package com.webgiadung.doanweb.controller.admin;

import com.webgiadung.doanweb.model.Brands;
import com.webgiadung.doanweb.utils.FileUtils;
import com.webgiadung.doanweb.services.BrandService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/add-brands")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 15    // 15MB
)
public class AddBrandController extends HttpServlet {
    private BrandService brandService = new BrandService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String brandName = request.getParameter("brandName");
            String brandCountry = request.getParameter("brandCountry");

            // DEBUG: Kiểm tra xem server có nhận được chữ không
            System.out.println("DEBUG - Nhận tên: " + brandName);
            System.out.println("DEBUG - Nhận quốc gia: " + brandCountry);

            if (brandName == null || brandName.isEmpty()) {
                out.print("{\"status\":\"error\", \"message\":\"Tên không được để trống!\"}");
                return;
            }
            Part filePart = request.getPart("brandLogo");

            String realPath = getServletContext().getRealPath("/");
            String fileName = FileUtils.saveFile(filePart, realPath, "brands");

            Brands brand = new Brands();
            brand.setName(brandName);
            brand.setCountry(brandCountry);
            brand.setLogo(fileName);

            int resultId = brandService.createBrand(brand);

            // DEBUG: Kiểm tra ID sau khi lưu
            System.out.println("DEBUG - ID sau lưu: " + resultId);

            if (resultId > 0) {
                out.print("{\"status\":\"success\", \"brandID\":" + resultId + ", \"brandName\":\"" + brandName + "\"}");
            } else if (resultId == 0) {
                out.print("{\"status\":\"error\", \"message\":\"Tên nhãn hiệu đã tồn tại!\"}");
            } else {
                out.print("{\"status\":\"error\", \"message\":\"Lỗi Database (Kiểm tra ID tự tăng trong Navicat)\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"status\":\"error\", \"message\":\"Lỗi hệ thống: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
            out.close();
        }
    }
}