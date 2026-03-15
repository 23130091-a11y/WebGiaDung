package com.webgiadung.webgiadung.controller.admin;

import com.webgiadung.webgiadung.model.Discounts;
import com.webgiadung.webgiadung.model.Categories;
import com.webgiadung.webgiadung.services.DiscountService;
import com.webgiadung.webgiadung.services.CategoriesService; // Đảm bảo đã import

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;

@WebServlet("/api/admin/discount-detail")
public class ViewDiscountController extends HttpServlet {
    private final DiscountService discountService = new DiscountService();
    private final CategoriesService categoriesService = new CategoriesService();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                out.print("{\"status\":\"error\", \"message\": \"ID không hợp lệ\"}");
                return;
            }

            int id = Integer.parseInt(idParam);
            Discounts d = discountService.getDiscountById(id);

            if (d != null) {

                String categoryName = "Tất cả sản phẩm";
                if (d.getId_cate() > 0) {
                    Categories cat = categoriesService.getCategory(d.getId_cate());
                    if (cat != null) {
                        categoryName = cat.getName();
                    }
                }

                StringBuilder json = new StringBuilder();
                json.append("{");
                json.append("\"status\": \"success\",");
                json.append("\"id\": ").append(d.getId()).append(",");
                json.append("\"name\": \"").append(escapeJson(d.getName())).append("\",");
                json.append("\"typeDiscount\": \"").append(d.getTypeDiscount()).append("\",");
                json.append("\"discount\": ").append(d.getDiscount()).append(",");
                json.append("\"description\": \"").append(escapeJson(d.getDescription())).append("\",");
                json.append("\"startDate\": \"").append(d.getStartDate().format(formatter)).append("\",");
                json.append("\"endDate\": \"").append(d.getEndDate().format(formatter)).append("\","); // CÓ DẤU PHẨY
                json.append("\"categoryName\": \"").append(escapeJson(categoryName)).append("\"");    // TRƯỜNG CUỐI KHÔNG DẤU PHẨY
                json.append("}");

                out.print(json.toString());
            } else {
                out.print("{\"status\":\"error\", \"message\": \"Không tìm thấy dữ liệu\"}");
            }
        } catch (Exception e) {
            response.setStatus(500);
            out.print("{\"status\":\"error\", \"message\": \"Lỗi: " + e.getMessage() + "\"}");
        }
    }

    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", " ").replace("\r", "");
    }
}