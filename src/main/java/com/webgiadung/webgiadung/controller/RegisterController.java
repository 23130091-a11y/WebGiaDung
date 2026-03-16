package com.webgiadung.webgiadung.controller;

import com.webgiadung.webgiadung.dao.AuthDao;
import com.webgiadung.webgiadung.dao.EmailVerificationDao;
import com.webgiadung.webgiadung.model.User;
import java.util.regex.Pattern;
import com.webgiadung.webgiadung.services.EmailService;
import com.webgiadung.webgiadung.utils.SecurityUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet("/register")
public class RegisterController extends HttpServlet {

    private static final Pattern EMAIL_RE =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private static final Pattern PHONE_RE =
            Pattern.compile("^0\\d{9,10}$"); // 10-11 số, bắt đầu 0

    private static final Pattern PASS_RE = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String repassword = request.getParameter("repassword");

        email = (email == null) ? "" : email.trim().toLowerCase();
        phone = (phone == null) ? "" : phone.trim();
        password = (password == null) ? "" : password;
        repassword = (repassword == null) ? "" : repassword;

        if (email.isEmpty() || phone.isEmpty() || password.isEmpty() || repassword.isEmpty()) {
            sendError(request, response, "Vui lòng nhập đầy đủ thông tin");
            return;
        }

        // validate data phía server bằng các regex
        if (!EMAIL_RE.matcher(email).matches()) {
            sendError(request, response, "Email không đúng định dạng");
            return;
        }

        if (!PHONE_RE.matcher(phone).matches()) {
            sendError(request, response, "Số điện thoại không hợp lệ");
            return;
        }

        if (!PASS_RE.matcher(password).matches()) {
            sendError(request, response, "Mật khẩu tối thiểu 8 ký tự, có chữ hoa, chữ thường, số và ký tự đặc biệt");
            return;
        }

        // check mk
        if (!password.equals(repassword)) {
            sendError(request, response, "Mật khẩu xác nhận không khớp");
            return;
        }

        // XỬ LÝ ĐĂNG KÝ
        try {
            User user = new User();
            user.setEmail(email);
            user.setPhone(phone);
            user.setPassword(SecurityUtils.hashMD5(password));
            user.setRole(0);
            user.setStatus(0); // Tài khoản chưa kích hoạt

            AuthDao authDao = new AuthDao();
            authDao.register(user);

            // tạo token ứng với email
            String token = java.util.UUID.randomUUID().toString();
            EmailVerificationDao evDao = new EmailVerificationDao();
            evDao.saveToken(email, token);

            // tạo link xác nhận
            String contextPath = request.getContextPath(); // Sẽ lấy được "/DoAnWeb"
            String verifyLink = "http://127.0.0.1:8080" + contextPath + "/test-verify?token=" + token;

            // gửi mail
            EmailService emailService = new EmailService();
            emailService.sendVerifyEmail(email, verifyLink);

            response.sendRedirect(request.getContextPath() + "/login.jsp?msg=verify");

        } catch (Exception ex) {
            ex.printStackTrace();
            sendError(request, response, "Hệ thống đang lỗi: " + ex.getMessage());
        }
    }

    private void sendError(HttpServletRequest request, HttpServletResponse response, String msg) throws ServletException, IOException {
        request.setAttribute("error", msg);
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
}
