package com.webgiadung.doanweb.controller.admin;

import com.webgiadung.doanweb.dao.ProductDao;
import com.webgiadung.doanweb.dao.DiscountDao; // Giả sử bạn có DiscountDao
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/admin/delete-discount")
public class DeleteDiscountController extends HttpServlet {

    private final ProductDao productDao = new ProductDao();
    private final DiscountDao discountDao = new DiscountDao(); // Khởi tạo Dao

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String idRaw = req.getParameter("id");
            if (idRaw == null || idRaw.isEmpty()) {
                resp.getWriter().write("{\"status\":\"error\", \"message\":\"ID không hợp lệ hoặc bị trống!\"}");
                return;
            }

            int discountId = Integer.parseInt(idRaw);

            // BƯỚC 1: Gỡ liên kết ở bảng products
            productDao.removeDiscount(discountId);

            // BƯỚC 2: Thực hiện xóa ở bảng discounts
            boolean isDeleted = discountDao.deleteDiscount(discountId);

            if (isDeleted) {
                resp.getWriter().write("{\"status\":\"success\"}");
            } else {
                // Trường hợp execute() chạy nhưng không có dòng nào bị xóa (sai ID)
                resp.getWriter().write("{\"status\":\"fail\", \"message\":\"Không tìm thấy bản ghi khuyến mãi này trong hệ thống để xóa.\"}");
            }

        } catch (NumberFormatException e) {
            resp.getWriter().write("{\"status\":\"error\", \"message\":\"ID phải là một chữ số.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            // Lấy thông báo lỗi chi tiết từ Database (ví dụ: lỗi khóa ngoại)
            String errorMsg = e.getMessage() != null ? e.getMessage() : "Lỗi không xác định khi truy cập Database.";

            // Làm sạch thông báo lỗi để tránh hỏng JSON
            String cleanMsg = errorMsg.replace("\"", "'").replace("\n", " ").trim();

            resp.getWriter().write("{\"status\":\"error\", \"message\":\"Không thể xóa: " + cleanMsg + "\"}");
        }
    }
    }