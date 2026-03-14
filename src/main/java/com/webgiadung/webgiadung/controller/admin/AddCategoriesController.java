package com.webgiadung.doanweb.controller.admin;

import com.webgiadung.doanweb.services.CategoriesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/add-category")
public class AddCategoriesController extends HttpServlet {

    private final CategoriesService categoriesService = new CategoriesService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Cấu hình encoding để nhận tiếng Việt
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // 2. Lấy tham số từ request (Khớp với params.append bên JS)
            String cateName = request.getParameter("cateName");
            String cateDesc = request.getParameter("cateDesc");

            // DEBUG: In ra console server để kiểm tra (Giống bên Brand)
            System.out.println("DEBUG CATEGORY - Nhận tên: " + cateName);
            System.out.println("DEBUG CATEGORY - Nhận mô tả: " + cateDesc);

            // 3. Validate dữ liệu
            if (cateName == null || cateName.trim().isEmpty()) {
                out.print("{\"status\":\"error\", \"message\":\"Tên danh mục không được để trống!\"}");
                return;
            }

            // 4. Gọi Service để lưu vào Database
            // Giả sử hàm insertCategory trả về ID (int) của dòng vừa thêm
            int resultId = categoriesService.insertCategory(cateName, cateDesc);

            // DEBUG: Kiểm tra ID sau khi lưu
            System.out.println("DEBUG CATEGORY - ID sau lưu: " + resultId);

            // 5. Trả về JSON kết quả
            if (resultId > 0) {
                // Xử lý dấu ngoặc kép trong tên để tránh lỗi cú pháp JSON
                String safeName = cateName.replace("\"", "\\\"");

                // Trả về đúng 3 thông tin JS cần: status, cateID, cateName
                out.print("{\"status\":\"success\", \"cateID\":" + resultId + ", \"cateName\":\"" + safeName + "\"}");
            } else if (resultId == 0) {
                out.print("{\"status\":\"error\", \"message\":\"Tên danh mục có thể đã tồn tại!\"}");
            } else {
                out.print("{\"status\":\"error\", \"message\":\"Lỗi Database (Kiểm tra ID trả về)\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"status\":\"error\", \"message\":\"Lỗi hệ thống: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
            out.close();
        }
    }
}