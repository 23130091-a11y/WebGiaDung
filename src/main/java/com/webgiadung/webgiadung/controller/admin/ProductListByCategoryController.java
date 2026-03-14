package com.webgiadung.doanweb.controller.admin;

import com.webgiadung.doanweb.model.Product;
import com.webgiadung.doanweb.services.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.StringJoiner;

@WebServlet("/api/products-by-category")
public class ProductListByCategoryController extends HttpServlet {

    // Khởi tạo Service
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Thiết lập header trả về JSON và Encoding UTF-8
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 2. Lấy tham số cateId từ URL
        String cateIdParam = request.getParameter("cateId");
        List<Product> list;

        try {
            if (cateIdParam != null && !cateIdParam.isEmpty() && !cateIdParam.equals("0")) {
                int cateId = Integer.parseInt(cateIdParam);
                list = productService.getProductsByCategory(cateId);
            } else {
                list = productService.getListProduct();
            }
        } catch (NumberFormatException e) {
            list = productService.getListProduct();
        }

        // 3. Tạo chuỗi JSON thủ công
        StringJoiner sj = new StringJoiner(",", "[", "]");

        for (Product p : list) {
            String name = (p.getName() != null) ? p.getName().replace("\"", "\\\"").replace("\n", " ") : "";
            String image = (p.getImage() != null) ? p.getImage().replace("\"", "\\\"") : "";

            // --- PHẦN ĐÃ THÊM: TRƯỜNG "post" ---
            // Thêm \"post\":%d vào chuỗi mẫu
            // Thêm p.getPost() vào danh sách tham số
            String item = String.format(
                    "{\"id\":%d,\"name\":\"%s\",\"image\":\"%s\",\"price\":%.0f,\"quantity\":%d,\"post\":%d}",
                    p.getId(),
                    name,
                    image,
                    p.getTotalPrice(),
                    p.getQuantity(),
                    p.getPost() // <--- QUAN TRỌNG: Lấy trạng thái (1 hoặc 0) để check checkbox
            );

            sj.add(item);
        }

        // 4. Trả kết quả
        PrintWriter out = response.getWriter();
        out.print(sj.toString());
        out.flush();
    }
}