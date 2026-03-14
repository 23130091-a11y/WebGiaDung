package com.webgiadung.doanweb.controller;

import com.webgiadung.doanweb.dao.AuthDao;
import com.webgiadung.doanweb.dao.EmailVerificationDao;
import com.webgiadung.doanweb.model.User;
import java.util.regex.Pattern;
import com.webgiadung.doanweb.services.EmailService;
import com.webgiadung.doanweb.utils.SecurityUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/register")
public class RegisterController extends HttpServlet {
    private static final Pattern EMAIL_RE =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final Pattern PHONE_RE =
            Pattern.compile("^0\\d{9,10}$"); // 10-11 số, bắt đầu 0

    // Thêm Regex
    private static final Pattern PASS_RE = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy và làm sạch dữ liệu
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String repassword = request.getParameter("repassword");

        email = (email == null) ? "" : email.trim().toLowerCase();
        phone = (phone == null) ? "" : phone.trim();
        password = (password == null) ? "" : password;
        repassword = (repassword == null) ? "" : repassword;

        // 1. Check trống
        if (email.isEmpty() || phone.isEmpty() || password.isEmpty() || repassword.isEmpty()) {
            sendError(request, response, "Vui lòng nhập đầy đủ thông tin");
            return;
        }

        // 2. Check định dạng Email & Phone
        if (!EMAIL_RE.matcher(email).matches()) {
            sendError(request, response, "Email không đúng định dạng");
            return;
        }
        if (!PHONE_RE.matcher(phone).matches()) {
            sendError(request, response, "Số điện thoại không hợp lệ");
            return;
        }

        // 3. Check độ mạnh mật khẩu
        if (!PASS_RE.matcher(password).matches()) {
            sendError(request, response, "Mật khẩu tối thiểu 8 ký tự, có chữ hoa, chữ thường, số và ký tự đặc biệt");
            return;
        }

        // 4. Check mật khẩu khớp (Sửa tại đây)
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
            // 1. Lưu User vào DB
            authDao.register(user);

            // 2. TẠO TOKEN VÀ LƯU VÀO BẢNG email_verification
            String token = java.util.UUID.randomUUID().toString();
            EmailVerificationDao evDao = new EmailVerificationDao();
            evDao.saveToken(email, token);

            // 3. TẠO LINK XÁC NHẬN
            // Dùng 127.0.0.1 để tránh lỗi "Từ chối kết nối" trên một số trình duyệt
            String contextPath = request.getContextPath(); // Sẽ lấy được "/DoAnWeb"
            String verifyLink = "http://127.0.0.1:8080" + contextPath + "/test-verify?token=" + token;

            // 4. GỌI SERVICE GỬI MAIL
            EmailService emailService = new EmailService();
            emailService.sendVerifyEmail(email, verifyLink);

            // 5. Chuyển hướng thông báo thành công
            response.sendRedirect(request.getContextPath() + "/login.jsp?msg=verify");

        } catch (Exception ex) {
            ex.printStackTrace();
            sendError(request, response, "Hệ thống đang lỗi: " + ex.getMessage());
        }
    }

    // Hàm phụ để code ngắn gọn hơn, tránh lặp lại requestDispatcher
    private void sendError(HttpServletRequest request, HttpServletResponse response, String msg) throws ServletException, IOException {
        request.setAttribute("error", msg);
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
}
