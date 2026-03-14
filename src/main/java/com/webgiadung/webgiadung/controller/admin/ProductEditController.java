package com.webgiadung.doanweb.controller.admin;

import com.webgiadung.doanweb.model.Product;
import com.webgiadung.doanweb.model.ProductDescriptions;
import com.webgiadung.doanweb.model.ProductDetails;
import com.webgiadung.doanweb.services.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/admin/product-edit"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB (Mỗi file tối đa 10MB)
        maxRequestSize = 1024 * 1024 * 50    // 50MB (Tổng request tối đa 50MB)
)
public class ProductEditController extends HttpServlet {

    private final ProductService productService = new ProductService();
    // Đường dẫn lưu ảnh (cần cấu hình phù hợp với server thực tế)
    private static final String UPLOAD_DIR = "uploads";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy thông tin sản phẩm để hiển thị lên form sửa
        String idStr = req.getParameter("id");
        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                // Dùng hàm lấy full info để có cả list desc và list detail
                Product product = productService.getProductFullInfo(id);

                req.setAttribute("product", product);
                // Cần load thêm danh sách categories, brands để đổ vào select box
                // req.setAttribute("categories", categoryService.getAll());
                // req.setAttribute("brands", brandService.getAll());

                req.getRequestDispatcher("/views/admin/product-edit.jsp").forward(req, resp);
            } catch (Exception e) {
                resp.sendRedirect(req.getContextPath() + "/admin/product-list?msg=error");
            }
        } else {
            resp.sendRedirect(req.getContextPath() + "/admin/product-list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        try {
            // 1. NHẬN THÔNG TIN CƠ BẢN CỦA SẢN PHẨM
            Product p = new Product();
            p.setId(Integer.parseInt(req.getParameter("id")));
            p.setName(req.getParameter("name"));
            p.setFirstPrice(Double.parseDouble(req.getParameter("price_first")));
            p.setTotalPrice(Double.parseDouble(req.getParameter("price_total")));
            p.setQuantity(Integer.parseInt(req.getParameter("quantity")));
            // Các trường ID khóa ngoại
            p.setCategoriesId(Integer.parseInt(req.getParameter("categories_id")));
            p.setBrandsId(Integer.parseInt(req.getParameter("brands_id")));
            // Xử lý logic Post (bài viết/trạng thái)
            String postVal = req.getParameter("post");
            p.setPost(postVal != null ? Integer.parseInt(postVal) : 0);

            // 2. XỬ LÝ ẢNH CHÍNH (MAIN IMAGE)
            Part mainImagePart = req.getPart("image");
            String oldImage = req.getParameter("old_image");

            String mainImageName = handleFileUpload(mainImagePart, req.getServletContext().getRealPath(""));
            if (mainImageName != null) {
                p.setImage(mainImageName); // Có upload ảnh mới
            } else {
                p.setImage(oldImage); // Giữ nguyên ảnh cũ
            }

            // 3. XỬ LÝ DANH SÁCH MÔ TẢ (DESCRIPTIONS)
            // Form ở Frontend cần đặt name dạng mảng: name="desc_id[]", name="desc_title[]"...
            List<ProductDescriptions> descList = new ArrayList<>();
            String[] descIds = req.getParameterValues("desc_id");
            String[] descTitles = req.getParameterValues("desc_title");
            String[] descContents = req.getParameterValues("desc_content");

            if (descIds != null) {
                for (int i = 0; i < descIds.length; i++) {
                    ProductDescriptions desc = new ProductDescriptions();
                    // Nếu là dòng mới thêm bằng JS thì ID có thể là 0 hoặc rỗng
                    desc.setId(parseId(descIds[i]));
                    desc.setTitle(descTitles[i]);
                    desc.setDescription(descContents[i]);
                    desc.setProductId(p.getId());
                    descList.add(desc);
                }
            }
            p.setDescriptionsList(descList);

            // 4. XỬ LÝ DANH SÁCH CHI TIẾT (DETAILS - CÓ ẢNH)
            List<ProductDetails> detailList = new ArrayList<>();
            String[] detailIds = req.getParameterValues("detail_id");
            String[] detailTitles = req.getParameterValues("detail_title");
            String[] detailDescs = req.getParameterValues("detail_desc");
            String[] detailOldImages = req.getParameterValues("detail_old_image");

            if (detailIds != null) {
                for (int i = 0; i < detailIds.length; i++) {
                    ProductDetails detail = new ProductDetails();
                    int dId = parseId(detailIds[i]);
                    detail.setId(dId);
                    detail.setTitle(detailTitles[i]);
                    detail.setDescription(detailDescs[i]);
                    detail.setProductId(p.getId());

                    // Xử lý ảnh cho từng dòng Detail
                    // Lưu ý: Input file cho detail nên đặt name="detail_image_ID" hoặc xử lý index khéo léo
                    // Ở đây giả sử Frontend đặt name="detail_image_0", "detail_image_1"... tương ứng index vòng lặp
                    Part detailPart = req.getPart("detail_image_" + i);
                    String detailImgName = handleFileUpload(detailPart, req.getServletContext().getRealPath(""));

                    if (detailImgName != null) {
                        detail.setImage(detailImgName);
                    } else {
                        // Nếu không up ảnh mới, lấy ảnh cũ từ input hidden (nếu có)
                        if (detailOldImages != null && i < detailOldImages.length) {
                            detail.setImage(detailOldImages[i]);
                        }
                    }
                    detailList.add(detail);
                }
            }
            p.setDetailsList(detailList);

            // 5. GỌI SERVICE ĐỂ CẬP NHẬT (Transaction handled in DAO)
            boolean isUpdated = productService.updateProduct(p);

            if (isUpdated) {
                resp.sendRedirect(req.getContextPath() + "/admin/product-list?msg=success");
            } else {
                req.setAttribute("error", "Cập nhật thất bại.");
                req.setAttribute("product", p);
                req.getRequestDispatcher("/views/admin/product-edit.jsp").forward(req, resp);
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            req.getRequestDispatcher("/views/admin/product-edit.jsp").forward(req, resp);
        }
    }

    private String handleFileUpload(Part part, String appPath) throws IOException {
        if (part == null || part.getSize() == 0 || part.getSubmittedFileName().isEmpty()) {
            return null;
        }

        String fileName = Path.of(part.getSubmittedFileName()).getFileName().toString();
        // Tạo tên file unique để tránh trùng lặp
        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;

        // Tạo thư mục nếu chưa tồn tại
        String savePath = appPath + File.separator + UPLOAD_DIR;
        File fileSaveDir = new File(savePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }

        // Ghi file
        part.write(savePath + File.separator + uniqueFileName);

        // Trả về đường dẫn để lưu vào DB (VD: uploads/anh1.jpg)
        return UPLOAD_DIR + "/" + uniqueFileName;
    }

    private int parseId(String idStr) {
        try {
            return Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            return 0; // Trả về 0 nếu là dòng mới thêm (chưa có ID)
        }
    }
}