package com.webgiadung.doanweb.controller;

import com.webgiadung.doanweb.model.Product;
import com.webgiadung.doanweb.model.Slide; // Giả định bạn có model Slide
import com.webgiadung.doanweb.services.ProductService;
import com.webgiadung.doanweb.services.SlideService; // Cần service để lấy thông tin Slide
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SlideController", value = "/view-slide")
public class SlideController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Lấy ID slide từ request
        String slideIdStr = request.getParameter("id");

        if (slideIdStr != null) {
            try {
                int slideId = Integer.parseInt(slideIdStr);

                SlideService slideService = new SlideService();
                Slide slide = slideService.getById(slideId);

                if (slide != null) {
                    String discountName = slide.getName();
                    ProductService productService = new ProductService();
                    List<Product> products = productService.searchByDiscountName(discountName);

                    // 4. Gửi dữ liệu sang JSP
                    request.setAttribute("slide", slide);
                    request.setAttribute("productList", products);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // Chuyển hướng sang trang slide.jsp
        request.getRequestDispatcher("/slide.jsp").forward(request, response);
    }
}