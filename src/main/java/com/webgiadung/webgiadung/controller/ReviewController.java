package com.webgiadung.doanweb.controller;

import com.webgiadung.doanweb.dao.ProductReviewDao;
import com.webgiadung.doanweb.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "ReviewController", value = "/review")
public class ReviewController extends HttpServlet {

    private final ProductReviewDao reviewDao = new ProductReviewDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("user");

        int productId = 0;
        try { productId = Integer.parseInt(req.getParameter("productId")); } catch (Exception ignored) {}

        // CHẶN: chưa login -> đá về login, đăng nhập xong quay lại đúng sản phẩm
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login?redirect=/product?id=" + productId + "#reviews");
            return;
        }

        String comment = req.getParameter("comment");
        if (comment == null || comment.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/product?id=" + productId + "&cmt_err=empty#reviews");
            return;
        }

        double rating = 5;
        try { rating = Double.parseDouble(req.getParameter("rating")); } catch (Exception ignored) {}
        if (rating < 1) rating = 1;
        if (rating > 5) rating = 5;

        reviewDao.insert(productId, user.getId(), rating, comment.trim());

        resp.sendRedirect(req.getContextPath() + "/product?id=" + productId + "#reviews");
    }
}
