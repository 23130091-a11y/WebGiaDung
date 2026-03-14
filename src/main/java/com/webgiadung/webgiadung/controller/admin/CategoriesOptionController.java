package com.webgiadung.doanweb.controller.admin;

import com.webgiadung.doanweb.model.Categories;
import com.webgiadung.doanweb.services.CategoriesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.StringJoiner;

@WebServlet("/api/categories")
public class CategoriesOptionController extends HttpServlet {

    private final CategoriesService categoriesService = new CategoriesService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Setup Header JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // 2. Lấy dữ liệu
            List<Categories> list = categoriesService.getAllCategories();

            // 3. Convert sang JSON thủ công
            StringJoiner sj = new StringJoiner(",", "[", "]");

            for (Categories c : list) {
                // Kiểm tra null để tránh lỗi 500
                String name = (c.getName() != null) ? c.getName().replace("\"", "\\\"") : "";

                // QUAN TRỌNG: Đảm bảo class Categories có hàm getId() và getName()
                String item = String.format("{\"id\":%d,\"name\":\"%s\"}", c.getId(), name);
                sj.add(item);
            }

            // 4. Trả về
            PrintWriter out = response.getWriter();
            out.print(sj.toString());
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
        }
    }
}