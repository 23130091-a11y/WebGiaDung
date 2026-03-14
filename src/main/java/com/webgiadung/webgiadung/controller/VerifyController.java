package com.webgiadung.doanweb.controller;

import com.webgiadung.doanweb.dao.AuthDao;
import com.webgiadung.doanweb.dao.EmailVerificationDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "VerifyController", value = "/test-verify")
public class VerifyController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");

        // 1. Kiểm tra token đầu vào
        if (token == null || token.isEmpty()) {
            System.err.println("LỖI: Token trống từ request!");
            response.sendRedirect(request.getContextPath() + "/login.jsp?msg=invalid");
            return;
        }

        EmailVerificationDao evDao = new EmailVerificationDao();
        // 2. Lấy email
        String email = evDao.getEmailByToken(token.trim());

        if (email == null) {
            // Nếu chạy vào đây, nghĩa là mã token trong Link và trong DB không khớp
            System.err.println("LỖI: Không tìm thấy email ứng với token: " + token);
            response.sendRedirect(request.getContextPath() + "/login.jsp?msg=invalid");
            return;
        }

        // 3. Kích hoạt user
        AuthDao authDao = new AuthDao();
        try {
            // Đảm bảo email truyền vào cũng sạch sẽ
            authDao.activateUser(email.trim().toLowerCase());
            System.out.println("--- THÀNH CÔNG: Đã kích hoạt cho email: " + email + " ---");

            // 4. Xóa token (Chỉ xóa sau khi chắc chắn đã kích hoạt thành công)
            evDao.deleteToken(token);

            // Chuyển hướng kèm thông báo thành công
            response.sendRedirect(request.getContextPath() + "/login.jsp?msg=verified");
        } catch (Exception e) {
            System.err.println("LỖI KHI CẬP NHẬT DB:");
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/login.jsp?msg=error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}