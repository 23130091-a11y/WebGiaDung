package com.webgiadung.doanweb.controller.admin;

import com.webgiadung.doanweb.model.Slide;
import com.webgiadung.doanweb.services.SlideService;
import com.webgiadung.doanweb.utils.FileUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/api/add-slide")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 10,
        maxFileSize = 1024 * 1024 * 20,
        maxRequestSize = 1024 * 1024 * 100
)
public class AddSlideController extends HttpServlet {
    private SlideService slideService = new SlideService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            // Kiểm tra dữ liệu đầu vào cơ bản
            String name = req.getParameter("name");
            if (name == null || name.trim().isEmpty()) {
                resp.getWriter().write("{\"status\":\"error\", \"message\":\"Tên slide không được để trống\"}");
                return;
            }

            Slide s = new Slide();
            s.setName(name);
            s.setText(req.getParameter("text"));

            s.setCreatedAt(LocalDateTime.now());
            s.setUpdatedAt(LocalDateTime.now());

            String statusRaw = req.getParameter("status");
            s.setStatus(("1".equals(statusRaw) || "active".equals(statusRaw)) ? 1 : 0);

            // Xử lý File
            String realPath = getServletContext().getRealPath("/");
            Part filePart = req.getPart("avatar");
            if (filePart != null && filePart.getSize() > 0) {
                String filePath = FileUtils.saveFile(filePart, realPath, "slides");
                s.setAvatar(filePath);
            } else {

                resp.getWriter().write("{\"status\":\"error\", \"message\":\"Vui lòng chọn ảnh cho slide\"}");
                return;
            }

            boolean isInserted = slideService.insert(s);

            if (isInserted) {
                resp.getWriter().write("{\"status\":\"success\"}");
            } else {
                resp.getWriter().write("{\"status\":\"fail\", \"message\":\"Lỗi hệ thống: Không thể ghi vào Database\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            String cleanMessage = (e.getMessage() != null)
                    ? e.getMessage().replace("\"", "'").replace("\n", " ")
                    : "Lỗi Server không xác định";
            resp.getWriter().write("{\"status\":\"error\", \"message\":\"" + cleanMessage + "\"}");
        }
    }
}