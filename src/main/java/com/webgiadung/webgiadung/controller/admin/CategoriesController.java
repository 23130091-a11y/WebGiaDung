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

@WebServlet("/api/categories-list")
public class CategoriesController extends HttpServlet {
    private final CategoriesService categoriesService = new CategoriesService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Cấu hình UTF-8 để không bị lỗi font tiếng Việt
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // 1. Lấy danh sách danh mục cha (những mục có parentId = 0 hoặc null trong DB)
            List<Categories> parents = categoriesService.getCategoriesParent();

            // 2. Tạo chuỗi JSON phân cấp thủ công
            StringJoiner sjParent = new StringJoiner(",", "[", "]");

            if (parents != null) {
                for (Categories p : parents) {
                    // Lấy các danh mục con có parentId trùng với ID của cha này
                    List<Categories> children = categoriesService.getCategoriesByParentId(p.getId());

                    // Tạo chuỗi JSON cho danh sách con
                    StringJoiner sjChild = new StringJoiner(",", "[", "]");
                    if (children != null) {
                        for (Categories c : children) {
                            sjChild.add(String.format("{\"id\":%d,\"name\":\"%s\"}",
                                    c.getId(), escapeJson(c.getName())));
                        }
                    }

                    // Đóng gói cha kèm theo danh sách con của nó
                    String parentJson = String.format(
                            "{\"id\":%d,\"name\":\"%s\",\"children\":%s}",
                            p.getId(),
                            escapeJson(p.getName()),
                            sjChild.toString()
                    );
                    sjParent.add(parentJson);
                }
            }

            PrintWriter out = response.getWriter();
            out.print(sjParent.toString());
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
        }
    }

    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\"", "\\\"");
    }
}