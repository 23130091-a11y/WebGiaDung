package com.webgiadung.webgiadung.controller.admin;

import com.webgiadung.webgiadung.dao.AuthDao;
import com.webgiadung.webgiadung.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {
        "/admin/customers",          // GET: list + search
        "/admin/customers/update",   // POST: update
        "/admin/customers/lock"      // POST: soft delete (status=0)
})
public class AdminCustomerController extends HttpServlet {

    private final AuthDao authDao = new AuthDao();

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;

        Object obj = session.getAttribute("user");
        if (!(obj instanceof User)) obj = session.getAttribute("USER_LOGIN");
        if (!(obj instanceof User)) return false;

        User ses = (User) obj;

        // reload từ DB để lấy role/status mới nhất
        User fresh = authDao.findByIdFull(ses.getId());
        if (fresh == null) return false;

        // cập nhật lại session luôn cho đồng bộ
        session.setAttribute("user", fresh);

        return fresh.getRole() == 1 && fresh.getStatus() == 1;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String q = request.getParameter("q");
        List<User> users = authDao.findUsers(q);

        request.setAttribute("users", users);
        request.setAttribute("q", q == null ? "" : q);


        request.setAttribute("tab", "customers");

        request.getRequestDispatcher("/admin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String uri = request.getRequestURI();

        if (uri.endsWith("/admin/customers/update")) {

            int id = parseIntSafe(request.getParameter("id"), -1);
            if (id == -1) {
                response.sendRedirect(request.getContextPath() + "/admin/customers?error=invalid_id");
                return;
            }

            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            if (phone != null) {
                phone = phone.trim();
                if (phone.isEmpty()) phone = null;
            }
            String address = request.getParameter("address");

            int role = parseIntSafe(request.getParameter("role"), 0);
            int status = parseIntSafe(request.getParameter("status"), 1);

            User u = new User();
            u.setId(id);
            u.setName(name);
            u.setEmail(email);
            u.setPhone(phone);
            u.setAddress(address);
            u.setRole(role);
            u.setStatus(status);

            String password = request.getParameter("password");
            if (password != null && !password.trim().isEmpty()) {
                u.setPassword(password.trim());
                authDao.adminUpdateUserWithPassword(u);
            } else {
                authDao.adminUpdateUser(u);
            }

        } else if (uri.endsWith("/admin/customers/lock")) {

            int id = parseIntSafe(request.getParameter("id"), -1);
            if (id != -1) {
                authDao.adminSoftDeleteUser(id);
            }
        }

        // REDIRECT DUY NHẤT 1 LẦN
        response.sendRedirect(request.getContextPath() + "/admin/customers");
    }

    private int parseIntSafe(String value, int defaultValue) {
        try {
            if (value == null || value.trim().isEmpty()) return defaultValue;
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
