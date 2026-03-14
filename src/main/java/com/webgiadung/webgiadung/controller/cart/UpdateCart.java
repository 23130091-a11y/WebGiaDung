package com.webgiadung.doanweb.controller.cart;

import com.webgiadung.doanweb.dao.CartDao;
import com.webgiadung.doanweb.dao.CartItemDao;
import com.webgiadung.doanweb.model.Cart;
import com.webgiadung.doanweb.model.CartItem;
import com.webgiadung.doanweb.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "UpdateCart", value = "/update-cart")
public class UpdateCart extends HttpServlet {

    private boolean isAjax(HttpServletRequest req) {
        String x = req.getHeader("X-Requested-With");
        return "XMLHttpRequest".equalsIgnoreCase(x) || "1".equals(req.getParameter("ajax"));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int productId;
        String action = request.getParameter("action");

        try {
            productId = Integer.parseInt(request.getParameter("productId"));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid productId");
            return;
        }

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) cart = new Cart();

        // update session cart
        if ("inc".equals(action)) cart.increaseQuantity(productId);
        else if ("dec".equals(action)) cart.decreaseQuantity(productId);

        session.setAttribute("cart", cart);

        int newQuantity = 0;
        double newSubtotal = 0;

        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId() == productId) {
                newQuantity = item.getQuantity();
                newSubtotal = item.getTotalPrice();
                break;
            }
        }

        double cartTotal = cart.getTotalPrice();
        int cartQty = cart.getTotalQuantity();

        // sync DB nếu login
        User user = (User) session.getAttribute("user");
        if (user != null) {
            Integer cartId = (Integer) session.getAttribute("CART_ID");
            CartDao cartDao = new CartDao();
            if (cartId == null) {
                cartId = cartDao.getOrCreateCartId(user.getId());
                session.setAttribute("CART_ID", cartId);
            }

            CartItemDao itemDao = new CartItemDao();
            if (newQuantity <= 0) itemDao.deleteItem(cartId, productId);
            else itemDao.setQuantity(cartId, productId, newQuantity);
        }

        // response
        if (isAjax(request)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print("{\"newQuantity\":" + newQuantity
                    + ",\"newSubtotal\":" + newSubtotal
                    + ",\"cartTotal\":" + cartTotal
                    + ",\"cartQty\":" + cartQty + "}");
            out.flush();
        } else {
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }
}
