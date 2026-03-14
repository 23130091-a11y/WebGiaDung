package com.webgiadung.doanweb.controller.cart;

import com.webgiadung.doanweb.dao.CartDao;
import com.webgiadung.doanweb.dao.CartItemDao;
import com.webgiadung.doanweb.model.Cart;
import com.webgiadung.doanweb.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "DeleteCart", value = "/delete-cart")
public class DeleteCart extends HttpServlet {

    private boolean isAjax(HttpServletRequest req) {
        String x = req.getHeader("X-Requested-With");
        return "XMLHttpRequest".equalsIgnoreCase(x) || "1".equals(req.getParameter("ajax"));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String raw = request.getParameter("id");
        int productId;
        try {
            productId = Integer.parseInt(raw);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) cart = new Cart();

        cart.deleteItem(productId);
        session.setAttribute("cart", cart);

        User user = (User) session.getAttribute("user");
        if (user != null) {
            Integer cartId = (Integer) session.getAttribute("CART_ID");
            CartDao cartDao = new CartDao();
            if (cartId == null) {
                cartId = cartDao.getOrCreateCartId(user.getId());
                session.setAttribute("CART_ID", cartId);
            }
            new CartItemDao().deleteItem(cartId, productId);
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String raw = request.getParameter("productId");
        if (raw == null) raw = request.getParameter("id");

        int productId;
        try {
            productId = Integer.parseInt(raw);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id");
            return;
        }

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) cart = new Cart();

        cart.deleteItem(productId);
        session.setAttribute("cart", cart);

        User user = (User) session.getAttribute("user");
        if (user != null) {
            Integer cartId = (Integer) session.getAttribute("CART_ID");
            CartDao cartDao = new CartDao();
            if (cartId == null) {
                cartId = cartDao.getOrCreateCartId(user.getId());
                session.setAttribute("CART_ID", cartId);
            }
            new CartItemDao().deleteItem(cartId, productId);
        }

        if (isAjax(request)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print("{\"status\":\"success\",\"cartQty\":" + cart.getTotalQuantity()
                    + ",\"cartTotal\":" + cart.getTotalPrice() + "}");
            out.flush();
            return;
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }
}
