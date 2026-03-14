package com.webgiadung.doanweb.controller.cart;

import com.webgiadung.doanweb.dao.CartDao;
import com.webgiadung.doanweb.dao.CartItemDao;
import com.webgiadung.doanweb.model.Cart;
import com.webgiadung.doanweb.model.CartItem;
import com.webgiadung.doanweb.model.Product;
import com.webgiadung.doanweb.model.User;
import com.webgiadung.doanweb.services.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

@WebServlet(name = "AddCart", value = "/add-cart")
public class AddCart extends HttpServlet {

    private boolean isAjax(HttpServletRequest req) {
        String x = req.getHeader("X-Requested-With");
        return "XMLHttpRequest".equalsIgnoreCase(x) || "1".equals(req.getParameter("ajax"));
    }

    private void writeJson(HttpServletResponse resp, int status, String json) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }

    private static String htmlEscape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private static String jsonEscape(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '\\': sb.append("\\\\"); break;
                case '"': sb.append("\\\""); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 32) sb.append(String.format("\\u%04x", (int) c));
                    else sb.append(c);
            }
        }
        return sb.toString();
    }

    /** Build lại HTML dropdown mini-cart để client update ngay (không cần reload). */
    private String buildMiniCartHtml(HttpServletRequest request, Cart cart) {
        String ctx = request.getContextPath();
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            return """
                <div class="cart-list--no-cart">
                    <img src="%s/assets/img/no-cart_img.png" class="header-cart__img" alt="">
                    <span class="header-cart__msg">Chưa có sản phẩm</span>
                </div>
            """.formatted(ctx);
        }

        DecimalFormat df = new DecimalFormat("###,###");

        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"cart-list__wrap\">")
                .append("<h3 class=\"cart-list__header\">Sản phẩm đã thêm</h3>")
                .append("<ul class=\"cart-list__list\">");

        for (CartItem item : cart.getItems()) {
            if (item == null || item.getProduct() == null) continue;

            Product p = item.getProduct();
            int pid = p.getId();

            String name = htmlEscape(p.getName());
            String img = htmlEscape(p.getImage()); // tên file (xxx.jpg / xxx.png)
            String cate = String.valueOf(p.getCategoriesId());

            String totalPrice;
            try {
                double tp = item.getTotalPrice();
                totalPrice = df.format(tp) + " đ";
            } catch (Exception e) {
                totalPrice = String.valueOf(item.getTotalPrice()) + " đ";
            }

            sb.append("<li class=\"cart-list__item\" id=\"mini-cart-item-").append(pid).append("\">");

            sb.append("<a href=\"").append(ctx).append("/product?id=").append(pid).append("\" ")
                    .append("style=\"display:flex; gap:10px; align-items:center; text-decoration:none; color:inherit; width:100%;\">");

            sb.append("<img src=\"").append(ctx).append("/assets/img/products/").append(img).append("\" ")
                    .append("alt=\"").append(name).append("\" class=\"cart-list__img\">");

            sb.append("<section class=\"cart-list__body\">")
                    .append("<div class=\"cart-list__info\">")
                    .append("<h4 class=\"cart-list__heading\">").append(name).append("</h4>")
                    .append("<div class=\"cart-list__price-wrap\">")
                    .append("<span class=\"cart-list__price\">").append(totalPrice).append("</span>")
                    .append("<span class=\"cart-list__multiply\">x</span>")
                    .append("<span class=\"cart-list__qnt\">").append(item.getQuantity()).append("</span>")
                    .append("</div></div>")
                    .append("<div class=\"cart-list__desc\">")
                    .append("<span class=\"cart-list__product-cate\">Phân loại: ").append(htmlEscape(cate)).append("</span>")
                    .append("</div></section></a>");

            sb.append("<a href=\"").append(ctx).append("/delete-cart?id=").append(pid).append("\" ")
                    .append("class=\"cart-list__remove\" ")
                    .append("onclick=\"event.preventDefault(); removeMiniCartItem(").append(pid).append(");\">Xóa</a>");

            sb.append("</li>");
        }

        sb.append("</ul>")
                .append("<a href=\"").append(ctx).append("/cart\" class=\"cart-list__view btn btn--default-color\">Xem giỏ hàng</a>")
                .append("</div>");

        return sb.toString();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        boolean ajax = isAjax(request);

        // 1) check login
        User user = (User) session.getAttribute("user");
        if (user == null) {
            if (ajax) writeJson(response, HttpServletResponse.SC_UNAUTHORIZED, "{\"status\":\"unauthorized\"}");
            else response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 2) validate params
        int productId;
        int quantity = 1;

        try {
            productId = Integer.parseInt(request.getParameter("productId"));
        } catch (Exception e) {
            if (ajax) writeJson(response, HttpServletResponse.SC_BAD_REQUEST,
                    "{\"status\":\"error\",\"message\":\"Invalid productId\"}");
            else response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid productId");
            return;
        }

        try { quantity = Integer.parseInt(request.getParameter("quantity")); }
        catch (Exception ignored) { }
        if (quantity <= 0) quantity = 1;

        // 3) get/create cart in session
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) cart = new Cart();

        // 4) get product
        ProductService productService = new ProductService();
        Product product = productService.getProduct(productId);
        if (product == null) {
            if (ajax) writeJson(response, HttpServletResponse.SC_NOT_FOUND,
                    "{\"status\":\"error\",\"message\":\"Product not found\"}");
            else response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
            return;
        }

        // 5) add to session cart
        cart.addItem(product, quantity);
        session.setAttribute("cart", cart);

        // 6) save to DB
        CartDao cartDao = new CartDao();
        CartItemDao itemDao = new CartItemDao();
        int cartId = cartDao.getOrCreateCartId(user.getId());
        itemDao.addOrInc(cartId, productId, quantity);
        session.setAttribute("CART_ID", cartId);

        // 7) response
        if (ajax) {
            String miniHtml = buildMiniCartHtml(request, cart);
            writeJson(response, HttpServletResponse.SC_OK,
                    "{\"status\":\"success\",\"cartQty\":" + cart.getTotalQuantity()
                            + ",\"miniCartHtml\":\"" + jsonEscape(miniHtml) + "\"}"
            );
        } else {
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
