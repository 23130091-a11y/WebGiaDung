package com.webgiadung.doanweb.controller.admin;

import com.webgiadung.doanweb.utils.FileUtils;
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

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/api/add-product")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 10,
        maxFileSize = 1024 * 1024 * 20,
        maxRequestSize = 1024 * 1024 * 100
)
public class AddProductController extends HttpServlet {
    private ProductService productService = new ProductService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            // --- 1. ĐỌC DỮ LIỆU CƠ BẢN ---
            String brandIdRaw = req.getParameter("brandID");
            String tagIdRaw = req.getParameter("tagID");
            String cateIdRaw = req.getParameter("cateID");
            String postStatusRaw = req.getParameter("postStatus");

            if (brandIdRaw == null || tagIdRaw == null || cateIdRaw == null || req.getParameter("productPrice") == null) {
                resp.getWriter().write("{\"status\":\"error\", \"message\":\"Thiếu dữ liệu bắt buộc (Nhãn hiệu, Tag, Danh mục hoặc Giá)\"}");
                return;
            }

            Product p = new Product();
            p.setName(req.getParameter("productName"));
            p.setFirstPrice(Double.parseDouble(req.getParameter("productPrice")));
            p.setTotalPrice(Double.parseDouble(req.getParameter("productPrice")));

            // Set ID các khóa ngoại
            p.setBrandsId(Integer.parseInt(brandIdRaw));
            p.setKeywordsId(Integer.parseInt(tagIdRaw));
            p.setCategoriesId(Integer.parseInt(cateIdRaw));

            p.setQuantity(Integer.parseInt(req.getParameter("productStock")));
            p.setPost("1".equals(postStatusRaw) ? 1 : 0);

            String realPath = getServletContext().getRealPath("/");

            // Ảnh chính
            Part mainPart = req.getPart("productImage");
            if (mainPart != null && mainPart.getSize() > 0) {
                p.setImage(FileUtils.saveFile(mainPart, realPath, "products"));
            }

            // --- 2. LƯU SẢN PHẨM & LẤY ID ---
            int productId = productService.addProduct(p);

            if (productId > 0) {
                // --- 3. LƯU MÔ TẢ (DESCRIPTIONS) ---
                String[] dTitles = req.getParameterValues("descTitles[]");
                String[] dContents = req.getParameterValues("descContents[]");

                if (dTitles != null && dContents != null) {
                    for (int i = 0; i < dTitles.length; i++) {
                        if (dTitles[i] != null && !dTitles[i].trim().isEmpty()) {
                            ProductDescriptions pd = new ProductDescriptions();
                            pd.setProductId(productId);
                            pd.setTitle(dTitles[i]);
                            pd.setDescription(dContents[i]);
                            productService.addDescription(pd);
                        }
                    }
                }

                // --- 4. LƯU CHI TIẾT (DETAILS) ---
                String[] detTitles = req.getParameterValues("detTitles[]");
                String[] detContents = req.getParameterValues("detContents[]");
                List<Part> detImages = req.getParts().stream()
                        .filter(part -> "detImages[]".equals(part.getName()) && part.getSize() > 0)
                        .collect(Collectors.toList());

                if (detTitles != null) {
                    for (int i = 0; i < detTitles.length; i++) {
                        ProductDetails detail = new ProductDetails();
                        detail.setProductId(productId);
                        detail.setTitle(detTitles[i]);
                        detail.setDescription(detContents[i]);

                        // Ảnh chi tiết
                        if (i < detImages.size()) {
                            detail.setImage(FileUtils.saveFile(detImages.get(i), realPath, "details"));
                        }
                        productService.addProductDetail(detail);
                    }
                }

                resp.getWriter().write("{\"status\":\"success\"}");
            } else {
                resp.getWriter().write("{\"status\":\"fail\", \"message\":\"Lỗi Database: Không tạo được ID sản phẩm\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // QUAN TRỌNG: Loại bỏ dấu ngoặc kép và ký tự xuống dòng trong message lỗi
            String cleanMessage = e.getMessage() != null
                    ? e.getMessage().replace("\"", "'").replace("\n", " ").replace("\r", "")
                    : "Unknown Server Error";
            resp.getWriter().write("{\"status\":\"error\", \"message\":\"" + cleanMessage + "\"}");
        }
    }
}