package com.webgiadung.doanweb.controller;

import com.webgiadung.doanweb.dao.ProductReviewDao;
import com.webgiadung.doanweb.model.Categories;
import com.webgiadung.doanweb.model.Product;
import com.webgiadung.doanweb.model.ProductReview;
import com.webgiadung.doanweb.services.CategoriesService;
import com.webgiadung.doanweb.services.ProductService;
import com.webgiadung.doanweb.utils.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductController", value = "/product")
public class ProductController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id");
            return;
        }

        ProductService pService = new ProductService();
        CategoriesService cService = new CategoriesService();

        Product p = pService.getProductFull(id);
        if (p == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // cookie lịch sử xem
        CookieUtils.addIdToCookie(request, response, "viewed_products", id);

        Categories category = cService.getCategory(p.getCategoriesId());
        List<Categories> parentCategories = cService.getCategoriesByParentId(p.getCategoriesId());

        ProductReviewDao reviewDao = new ProductReviewDao();
        List<ProductReview> reviews = reviewDao.findByProductId(id);

        request.setAttribute("product", p);
        request.setAttribute("category", category);
        request.setAttribute("parentCategories", parentCategories);
        request.setAttribute("reviews", reviews);

        request.getRequestDispatcher("/product.jsp").forward(request, response);
    }
}
