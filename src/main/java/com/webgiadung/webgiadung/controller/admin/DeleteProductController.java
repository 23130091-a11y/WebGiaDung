package com.webgiadung.doanweb.controller.admin;

import com.webgiadung.doanweb.services.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

// Định nghĩa URL mapping. Frontend sẽ gọi vào đường dẫn này.
@WebServlet(urlPatterns = {"/admin/product-delete"})
public class DeleteProductController extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Cấu hình phản hồi dạng JSON và encoding UTF-8
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            // 1. Lấy ID từ request
            String idParam = req.getParameter("id");

            if (idParam == null || idParam.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"status\": \"error\", \"message\": \"Thiếu ID sản phẩm.\"}");
                return;
            }

            int id = Integer.parseInt(idParam);

            // 2. Gọi Service để xóa (Hàm này đã viết ở bước trước, có Transaction)
            boolean isDeleted = productService.deleteProduct(id);

            // 3. Trả về kết quả cho Frontend
            if (isDeleted) {
                out.print("{\"status\": \"success\", \"message\": \"Xóa thành công.\"}");
            } else {
                // Xóa thất bại (ID không tồn tại hoặc lỗi DB)
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"status\": \"fail\", \"message\": \"Không thể xóa sản phẩm này. Có thể ID không tồn tại.\"}");
            }

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"status\": \"error\", \"message\": \"ID không hợp lệ.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"status\": \"error\", \"message\": \"Lỗi Server: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }

    // Nếu lỡ gọi bằng phương thức GET thì chặn lại hoặc chuyển sang POST (tùy chọn)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}