package com.webgiadung.webgiadung.controller.cart;

import com.webgiadung.webgiadung.model.Product;
import com.webgiadung.webgiadung.model.Slide;
import com.webgiadung.webgiadung.services.ProductService;
import com.webgiadung.webgiadung.services.SlideService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
                    String discountName = slide.getTitle();
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