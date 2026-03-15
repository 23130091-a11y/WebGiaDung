package com.webgiadung.webgiadung.filter;

import com.webgiadung.webgiadung.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebFilter("/*")
public class AdminFilter implements Filter {

    private boolean isAdminRequest(HttpServletRequest req) {
        String uri = req.getRequestURI();              // /DoAnWeb/order-admin
        String ctx = req.getContextPath();             // /DoAnWeb
        String path = uri.substring(ctx.length());     // /order-admin

        // ✅ Tất cả URL admin của bạn sẽ được chặn ở đây
        return path.equals("/admin.jsp")
                || path.startsWith("/admin")           // /admin/* (vd /admin/customers)
                || path.endsWith("-admin");            // /order-admin, /product-admin, ...
    }

    private User getLoggedUser(HttpSession session) {
        if (session == null) return null;

        Object u1 = session.getAttribute("user");
        if (u1 instanceof User) return (User) u1;

        Object u2 = session.getAttribute("USER_LOGIN");
        if (u2 instanceof User) return (User) u2;

        return null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // Không phải request admin => cho đi luôn
        if (!isAdminRequest(req)) {
            chain.doFilter(request, response);
            return;
        }

        // Chặn cache để logout/back không xem lại trang admin cũ
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);

        HttpSession session = req.getSession(false);
        User user = getLoggedUser(session);

        // Chưa login
        if (user == null) {
            String ctx = req.getContextPath();
            String path = req.getRequestURI().substring(ctx.length());
            String redirect = URLEncoder.encode(path, StandardCharsets.UTF_8);
            resp.sendRedirect(ctx + "/login?redirect=" + redirect);
            return;
        }

        // Bị khóa / chưa kích hoạt
        if (user.getStatus() == 0) {
            if (session != null) session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Không phải admin
        if (user.getRole() != 1) {
            resp.sendRedirect(req.getContextPath() + "/list-product");
            return;
        }

        // OK admin
        chain.doFilter(request, response);
    }
}
