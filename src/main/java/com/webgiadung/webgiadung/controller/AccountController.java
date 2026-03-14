package com.webgiadung.doanweb.controller;

import com.webgiadung.doanweb.dao.OrderDao;
import com.webgiadung.doanweb.dao.AuthDao;
import com.webgiadung.doanweb.model.User;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.*;

import com.webgiadung.doanweb.model.Cart;
import com.webgiadung.doanweb.model.Product;

@WebServlet("/account")
public class AccountController extends HttpServlet {

    private final OrderDao orderDao = new OrderDao();
    private final AuthDao authDao = new AuthDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            // tab hiện tại (để JSP dùng highlight)
            String tab = req.getParameter("tab");
            if (tab == null || tab.isBlank()) tab = "all";
            req.setAttribute("tab", tab);

            // 1) Lấy tất cả đơn của user
            List<Map<String, Object>> ordersAll = orderDao.findOrdersByUser(user.getId());

            // 2) Chia theo trạng thái (DB của bạn: 0 xử lý, 1 đã giao, 3 đã hủy)
            List<Map<String, Object>> ordersProcessing = new ArrayList<>();
            List<Map<String, Object>> ordersDelivered  = new ArrayList<>();
            List<Map<String, Object>> ordersCancelled  = new ArrayList<>();

            for (Map<String, Object> o : ordersAll) {
                int st = toInt(o.get("status_transport"));

                if (st == 1) {
                    ordersDelivered.add(o);
                } else if (st == 3) {
                    ordersCancelled.add(o);
                } else { // mặc định 0/khác => xử lý
                    ordersProcessing.add(o);
                }
            }

            // 3) Map items theo orderId
            Map<Integer, List<Map<String, Object>>> orderItemsMap = new HashMap<>();
            for (Map<String, Object> o : ordersAll) {
                int orderId = toInt(o.get("id"));
                orderItemsMap.put(orderId, orderDao.findItemsByOrder(orderId));
            }

            // 4) Đẩy dữ liệu sang JSP
            req.setAttribute("ordersAll", ordersAll);
            req.setAttribute("ordersProcessing", ordersProcessing);
            req.setAttribute("ordersDelivered", ordersDelivered);
            req.setAttribute("ordersCancelled", ordersCancelled);
            req.setAttribute("orderItemsMap", orderItemsMap);

            req.getRequestDispatcher("/account.jsp").forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        // ORDER
// DB status_transport: 0 = đang xử lí, 1 = đã giao, 3 = đã hủy
        if ("cancelOrder".equals(action)) {
            int orderId = toInt(req.getParameter("orderId"));

            boolean ok = orderDao.cancelOrder(orderId, user.getId());
            if (ok) session.setAttribute("orderMsg", "Đã hủy đơn hàng #" + orderId);
            else session.setAttribute("orderError", "Không thể hủy đơn (đơn đã giao/đã hủy hoặc không hợp lệ).");

            resp.sendRedirect(req.getContextPath() + "/account?tab=cancelled");
            return;
        }

        if ("repurchase".equals(action)) {
            int orderId = toInt(req.getParameter("orderId"));

            // bảo mật: chỉ mua lại order thuộc user
            if (!orderDao.isOrderOwnedByUser(orderId, user.getId())) {
                session.setAttribute("orderError", "Đơn hàng không hợp lệ.");
                resp.sendRedirect(req.getContextPath() + "/account");
                return;
            }

            // tuỳ chọn: replaceCart=1 -> xóa giỏ hiện tại rồi mới thêm
            boolean replaceCart = "1".equals(req.getParameter("replaceCart"));

            List<Map<String, Object>> items = orderDao.findItemsForRepurchase(orderId);

            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null || replaceCart) cart = new Cart();

            int addedCount = 0;

            for (Map<String, Object> row : items) {
                int productId = toInt(row.get("product_id"));
                int qty = toInt(row.get("quantity"));
                if (productId <= 0 || qty <= 0) continue;

                Product p = new Product();
                p.setId(productId);
                p.setName(row.get("name") == null ? "" : String.valueOf(row.get("name")));
                p.setImage(row.get("image") == null ? "assets/img/no-image.png" : String.valueOf(row.get("image")));

                double firstPrice = toDouble(row.get("first_price"));
                double salePrice  = toDouble(row.get("total_price"));
                if (firstPrice <= 0) firstPrice = salePrice;
                if (salePrice  <= 0) salePrice  = firstPrice;

                p.setFirstPrice(firstPrice);
                p.setTotalPrice(salePrice);

                cart.addItem(p, qty);
                addedCount++;
            }

            session.setAttribute("cart", cart);

            if (addedCount > 0) {
                session.setAttribute("orderMsg", "Đã thêm sản phẩm từ đơn #" + orderId + " vào giỏ hàng.");
            } else {
                session.setAttribute("orderError", "Không tìm thấy sản phẩm để mua lại (sản phẩm có thể đã bị xoá).");
            }

            // redirect=checkout -> sang thẳng trang checkout; mặc định về giỏ
            String redirect = trim(req.getParameter("redirect"));
            if ("checkout".equalsIgnoreCase(redirect)) {
                resp.sendRedirect(req.getContextPath() + "/checkout");
            } else {
                resp.sendRedirect(req.getContextPath() + "/cart");
            }
            return;
        }


        if ("updateProfile".equals(action)) {
            String name = trim(req.getParameter("name"));
            String phone = trim(req.getParameter("phone"));
            String address = trim(req.getParameter("address"));

            if (name.isEmpty()) name = user.getName();
            if (phone.isEmpty()) phone = user.getPhone();

            if (name == null || name.trim().isEmpty()) {
                req.setAttribute("profileError", "Tên không được để trống!");
                doGet(req, resp);
                return;
            }

            boolean ok = authDao.updateProfile(user.getId(), name, phone, address);
            if (ok) {
                User fresh = authDao.findByIdFull(user.getId());
                if (fresh != null) session.setAttribute("user", fresh);
                req.setAttribute("profileMsg", "Cập nhật thông tin thành công!");
            } else {
                req.setAttribute("profileError", "Cập nhật thất bại, thử lại!");
            }

            doGet(req, resp);
            return;
        }

        if ("changePassword".equals(action)) {
            String oldPass = trim(req.getParameter("oldPassword"));
            String newPass = trim(req.getParameter("newPassword"));
            String confirm = trim(req.getParameter("confirmPassword"));

            if (oldPass.isEmpty() || newPass.isEmpty() || confirm.isEmpty()) {
                req.setAttribute("passError", "Vui lòng nhập đầy đủ các ô mật khẩu!");
                doGet(req, resp);
                return;
            }
            if (!newPass.equals(confirm)) {
                req.setAttribute("passError", "Mật khẩu mới và xác nhận không khớp!");
                doGet(req, resp);
                return;
            }
            if (!authDao.checkPassword(user.getId(), oldPass)) {
                req.setAttribute("passError", "Mật khẩu hiện tại không đúng!");
                doGet(req, resp);
                return;
            }

            boolean ok = authDao.updatePassword(user.getId(), newPass);
            if (ok) req.setAttribute("passMsg", "Đổi mật khẩu thành công!");
            else req.setAttribute("passError", "Đổi mật khẩu thất bại, thử lại!");

            doGet(req, resp);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/account");
    }

    private String trim(String s) {
        return s == null ? "" : s.trim();
    }

    private int toInt(Object v) {
        if (v == null) return 0;
        if (v instanceof Number) return ((Number) v).intValue();
        try { return Integer.parseInt(v.toString()); } catch (Exception e) { return 0; }
    }
    private double toDouble(Object v) {
        if (v == null) return 0.0;
        if (v instanceof Number) return ((Number) v).doubleValue();
        try { return Double.parseDouble(v.toString()); } catch (Exception e) { return 0.0; }
    }
}
