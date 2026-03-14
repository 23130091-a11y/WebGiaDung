package com.webgiadung.doanweb.controller.admin;

import com.webgiadung.doanweb.model.Discounts;
import com.webgiadung.doanweb.services.DiscountService;
import com.webgiadung.doanweb.services.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@WebServlet("/admin/add-discount")
@MultipartConfig // Quan trọng để đọc được dữ liệu text từ FormData gửi lên
public class AddDiscountController extends HttpServlet {

    private final DiscountService discountService = new DiscountService();
    private final ProductService productService = new ProductService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Đọc dữ liệu
            String name = request.getParameter("eventName");
            String discountValueRaw = request.getParameter("discountValue");
            String startDateRaw = request.getParameter("startDate");
            String endDateRaw = request.getParameter("endDate");
            String scope = request.getParameter("applyScope");
            String type = request.getParameter("discountType");
            String catIdRaw = request.getParameter("applyCategories");

            // KIỂM TRA NULL/EMPTY trước khi parse để tránh lỗi 500
            if (name == null || name.isBlank() || discountValueRaw == null || startDateRaw == null || endDateRaw == null) {
                response.getWriter().write("{\"status\":\"error\", \"message\":\"Vui lòng điền đầy đủ thông tin!\"}");
                return;
            }

            int idCate = 0;
            if ("category".equals(scope)) {
                idCate = (catIdRaw != null && !catIdRaw.isEmpty()) ? Integer.parseInt(catIdRaw) : 0;
            }

            double value = Double.parseDouble(discountValueRaw);

            // Bẫy lỗi parse ngày tháng
            LocalDateTime start = LocalDate.parse(startDateRaw).atStartOfDay();
            LocalDateTime end = LocalDate.parse(endDateRaw).atTime(23, 59, 59);

            Discounts d = new Discounts();
            d.setName(name);
            d.setDiscount(value);
            d.setStartDate(start);
            d.setEndDate(end);
            d.setTypeDiscount("percentage".equals(type) ? "1" : "2");
            d.setId_cate(idCate);

            int newDiscountId = discountService.insertDiscount(d);

            if (newDiscountId > 0) {
                if ("category".equals(scope) && idCate > 0) {
                    productService.applyDiscountToCategory(idCate, newDiscountId);
                } else if ("all".equals(scope)) {
                    productService.applyDiscountToAll(newDiscountId);
                }
                response.getWriter().write("{\"status\":\"success\"}");
            } else {
                response.getWriter().write("{\"status\":\"error\", \"message\":\"Lỗi Database: Không thể lưu Discount\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Không setStatus(500) nếu bạn muốn trả về thông báo JSON sạch cho Client
            response.getWriter().write("{\"status\":\"error\", \"message\":\"Lỗi Server: " + e.getMessage() + "\"}");
        }
    }}