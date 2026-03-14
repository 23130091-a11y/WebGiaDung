package com.webgiadung.doanweb.controller.admin;

import com.webgiadung.doanweb.model.Brands;
import com.webgiadung.doanweb.services.BrandService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.StringJoiner;

@WebServlet("/api/brands")
public class BrandOptionController extends HttpServlet {
    private BrandService brandService = new BrandService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Thiết lập header trả về JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<Brands> list = brandService.getAllBrands();

        // Tạo JSON thủ công bằng StringJoiner (Có sẵn từ Java 8+)
        StringJoiner sj = new StringJoiner(",", "[", "]");

        for (Brands b : list) {
            String name = (b.getName() != null) ? b.getName().replace("\"", "\\\"") : "";
            String item = String.format("{\"id\":%d,\"name\":\"%s\"}", b.getId(), name);
            sj.add(item);
        }

        PrintWriter out = response.getWriter();
        out.print(sj.toString());
        out.flush();
    }


}