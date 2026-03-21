package com.webgiadung.webgiadung.controller.cart;

import com.webgiadung.webgiadung.model.Cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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
        processDelete(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processDelete(request, response);
    }

    private void processDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String raw = request.getParameter("productId"); // post
        if (raw == null) raw = request.getParameter("id"); // get

        int productId;
        try {
            productId = Integer.parseInt(raw);
        } catch (Exception e) {
            if(isAjax(request)) {
                response.setStatus(400); // báo lỗi 400
                response.getWriter().print("{\"status\":\"error\"}");
            }
            else {
                response.sendRedirect(request.getContextPath() + "/cart");
            }
            return;
        }

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) cart = new Cart();

        // xóa sp ra khỏi session
        cart.deleteItem(productId);
        session.setAttribute("cart", cart);

        if (isAjax(request)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            java.text.DecimalFormat df = new java.text.DecimalFormat("###,###");
            String formattedTotal = df.format(cart.getTotalPrice()) + " đ";

            PrintWriter out = response.getWriter();
            out.print("{"
                    + "\"status\":\"success\","
                    + "\"cartQty\":" + cart.getTotalQuantity() + ","
                    + "\"cartTotal\":\"" + formattedTotal + "\""
                    + "}");
            out.flush();
            return;
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }
}
