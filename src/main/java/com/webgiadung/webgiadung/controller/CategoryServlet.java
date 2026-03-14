package com.webgiadung.doanweb.controller;

import com.webgiadung.doanweb.dao.CategoriesDao;
import com.webgiadung.doanweb.dao.ProductDao;
import com.webgiadung.doanweb.model.Categories;
import com.webgiadung.doanweb.model.Product;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CategoryServlet", value = "/category")
public class CategoryServlet extends HttpServlet {

    private CategoriesDao categoriesDao = new CategoriesDao();
    private ProductDao productDao = new ProductDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");

        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/list-product");
            return;
        }

        int categoryId;
        try {
            categoryId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/list-product");
            return;
        }

        Categories category = categoriesDao.getCategory(categoryId);
        List<Product> products = productDao.getProductsByCategoryId(categoryId);

        request.setAttribute("category", category);
        request.setAttribute("products", products);

        // dùng chung view
        request.getRequestDispatcher("/search.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}