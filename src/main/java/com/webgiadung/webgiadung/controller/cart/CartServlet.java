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
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "CartServlet", value = "/cart")
public class CartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("user");

        // 1) Lấy cart hiện có trong session (guest) nếu có
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) cart = new Cart();

        // 2) Nếu đã login -> load cart từ DB, build lại Cart rồi lưu vào session
        if (user != null) {
            CartDao cartDao = new CartDao();
            CartItemDao itemDao = new CartItemDao();
            ProductService productService = new ProductService();

            int cartId = cartDao.getOrCreateCartId(user.getId());
            session.setAttribute("CART_ID", cartId);

            var rows = itemDao.findItems(cartId);

            cart = new Cart(); // build mới từ DB
            for (var r : rows) {
                Product p = productService.getProduct(r.productId);
                if (p != null) cart.addItem(p, r.quantity);
            }

            session.setAttribute("cart", cart);
        } else {
            // guest: vẫn giữ cart trong session
            session.setAttribute("cart", cart);
        }

        // 3) Group theo ngày để cart.jsp hiển thị như cũ
        Map<LocalDate, List<CartItem>> itemsByDate =
                cart.getItems().stream()
                        .collect(Collectors.groupingBy(item -> item.getAddedAt().toLocalDate()));

        session.setAttribute("itemsByDate", itemsByDate);

        // 4) Forward (đặt cuối cùng)
        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/cart");
    }
}
