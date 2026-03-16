package com.webgiadung.webgiadung.controller;

import com.webgiadung.webgiadung.dao.AuthDao;
import com.webgiadung.webgiadung.dao.EmailVerificationDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
// servlet này: verify email -> kích hoạt tk user khi đăng ký thành công
@WebServlet(name = "VerifyController", value = "/test-verify")
public class VerifyController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");

        if (token == null || token.isEmpty()) {
            System.err.println("LỖI: Token trống từ request!");
            response.sendRedirect(request.getContextPath() + "/login.jsp?msg=invalid");
            return;
        }

        EmailVerificationDao evDao = new EmailVerificationDao();

        String email = evDao.getEmailByToken(token.trim());

        if (email == null) {
            System.err.println("LỖI: Không tìm thấy email ứng với token: " + token);
            response.sendRedirect(request.getContextPath() + "/login.jsp?msg=invalid");
            return;
        }

        AuthDao authDao = new AuthDao();
        try {
            authDao.activateUser(email.trim().toLowerCase());
            evDao.deleteToken(token);
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