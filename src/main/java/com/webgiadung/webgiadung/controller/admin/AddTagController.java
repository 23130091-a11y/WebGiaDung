package com.webgiadung.doanweb.controller.admin;

import com.webgiadung.doanweb.services.KeywordService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/add-tag")
public class AddTagController extends HttpServlet {
    private KeywordService keywordService = new KeywordService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String name = request.getParameter("tagName");
            String desc = request.getParameter("tagDesc");

            // Gọi Service thực tế
            int newId = keywordService.createKeyword(name, desc);

            if (newId > 0) {
                // Trả về JSON thành công cùng với ID và Name để UI cập nhật
                out.print("{\"status\":\"success\", \"tagID\":" + newId + ", \"tagName\":\"" + name + "\"}");
            } else if (newId == 0) {
                out.print("{\"status\":\"error\", \"message\":\"Từ khóa này đã tồn tại!\"}");
            } else {
                out.print("{\"status\":\"error\", \"message\":\"Dữ liệu không hợp lệ hoặc lỗi hệ thống.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"status\":\"error\", \"message\":\"Lỗi: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
            out.close();
        }
    }
}