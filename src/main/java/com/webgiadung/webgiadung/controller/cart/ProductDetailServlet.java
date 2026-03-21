package com.webgiadung.webgiadung.controller.cart;

import com.webgiadung.webgiadung.model.Product;
import com.webgiadung.webgiadung.services.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
// lấy id sản phẩm được chọn và hiển thị chi tiết
@WebServlet(name = "ProductDetailServlet", value = "/product-detail")
public class ProductDetailServlet extends HttpServlet {
    private final ProductService productService = new ProductService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idRaw = request.getParameter("id");

        // kiểm tra id
        if (idRaw == null || idRaw.trim().isEmpty()) {
            response.sendRedirect("index.jsp");
            return;
        }

        try {
            int id = Integer.parseInt(idRaw.trim());
            Product product = productService.getProductFullInfo(id);

            if (product != null) {
                request.setAttribute("product", product);
                request.getRequestDispatcher("/product.jsp").forward(request, response);
            } else {
                response.sendRedirect("index.jsp");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("index.jsp");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}