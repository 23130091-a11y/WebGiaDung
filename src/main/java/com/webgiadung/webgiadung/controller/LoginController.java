package com.webgiadung.webgiadung.controller;

import com.webgiadung.webgiadung.model.User;
import com.webgiadung.webgiadung.services.AuthService;
import com.webgiadung.webgiadung.utils.SecurityUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "LoginController", value = "/login")
public class LoginController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("redirect", request.getParameter("redirect"));
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Làm sạch đầu vào (tránh lỗi null hoặc khoảng trắng)
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        email = (email == null) ? "" : email.trim().toLowerCase();
        password = (password == null) ? "" : password;

        AuthService authService = new AuthService();

        // Băm mật khẩu để so sánh
        String hashedInput = SecurityUtils.hashMD5(password);

        // Gọi Service (Lúc này AuthService chỉ nên check Email & Password)
        User user = authService.checkLogin(email, hashedInput);

        // 1) Kiểm tra nếu sai tài khoản hoặc sai mật khẩu
        if (user == null) {
            request.setAttribute("error", "Email hoặc mật khẩu không chính xác!");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // 2) Kiểm tra trạng thái tài khoản (Sau khi đã xác nhận đúng Pass)
        // Nếu status = 0 (Chưa verify email hoặc bị Admin khóa)
        if (user.getStatus() == 0) {
            request.setAttribute("error", "Tài khoản chưa được kích hoạt hoặc đã bị khóa.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // 3) Đăng nhập thành công -> Lưu session
        HttpSession session = request.getSession(true);
        session.setAttribute("user", user);
        session.setAttribute("USER_LOGIN", user);


        String redirect = request.getParameter("redirect");

        if (redirect != null) redirect = redirect.trim();
        if (redirect != null && (!redirect.startsWith("/") || redirect.startsWith("//") || redirect.contains("://"))) {
            redirect = null;
        }

        if (redirect != null) {
            boolean isAdminPath = redirect.equals("/admin.jsp") || redirect.startsWith("/admin") || redirect.endsWith("-admin");
            if (isAdminPath && user.getRole() != 1) {
                redirect = null;
            }
        }

        if (redirect != null && !redirect.isBlank()) {
            response.sendRedirect(request.getContextPath() + redirect);
            return;
        }

        // 4) Điều hướng theo vai trò (Role)
        if (user.getRole() == 1) { // 1 là Admin
            response.sendRedirect(request.getContextPath() + "/admin/customers");
        } else { // 0 là User
            response.sendRedirect(request.getContextPath() + "/list-product");
        }
    }
}