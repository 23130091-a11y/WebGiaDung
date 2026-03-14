package com.webgiadung.doanweb.controller;

import com.webgiadung.doanweb.dao.BlogDao;
import com.webgiadung.doanweb.model.Blog;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "BlogDetailController", urlPatterns = {"/blog-detail"})
public class BlogDetailController extends HttpServlet {

    private final BlogDao blogDao = new BlogDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        int id;

        try {
            id = Integer.parseInt(idStr);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/list-product");
            return;
        }

        Blog blog = blogDao.findById(id);
        if (blog == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        request.setAttribute("blog", blog);
        request.getRequestDispatcher("/blog-detail.jsp").forward(request, response);
    }
}
