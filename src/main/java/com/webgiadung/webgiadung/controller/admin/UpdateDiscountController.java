package com.webgiadung.doanweb.controller.admin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.webgiadung.doanweb.model.Discounts;
import com.webgiadung.doanweb.services.DiscountService;
import com.webgiadung.doanweb.services.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/api/admin/update-discount")
public class UpdateDiscountController extends HttpServlet {
    private final DiscountService discountService = new DiscountService();
    private final ProductService productService = new ProductService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            JsonObject data = JsonParser.parseReader(req.getReader()).getAsJsonObject();
            int discountId = data.get("id").getAsInt();

            // 1. Cập nhật thông tin cơ bản của mã giảm giá
            Discounts discount = discountService.getDiscountById(discountId);
            if (discount == null) {
                resp.getWriter().print("{\"success\": false, \"message\": \"Mã không tồn tại\"}");
                return;
            }

            discount.setName(data.get("name").getAsString());
            discount.setDescription(data.get("description").getAsString());
            discount.setDiscount(data.get("value").getAsDouble());
            discount.setTypeDiscount(String.valueOf(data.get("type").getAsString().equals("percentage") ? 1 : 2));
            discount.setStartDate(LocalDate.parse(data.get("startDate").getAsString()).atStartOfDay());
            discount.setEndDate(LocalDate.parse(data.get("endDate").getAsString()).atStartOfDay());

            boolean updateSuccess = discountService.updateDiscount(discount);

            if (updateSuccess) {
                productService.removeDiscount(discountId);

                String scope = data.get("scope").getAsString();
                if ("all".equals(scope)) {
                    productService.applyDiscountToAll(discountId);
                } else if ("category".equals(scope)) {
                    int catId = data.get("categoryId").getAsInt();
                    productService.applyDiscountToCategory(catId, discountId);
                } else if ("specific".equals(scope)) {
                    // Xử lý danh sách ID sản phẩm cụ thể (nếu bạn gửi mảng productIds lên)
                }

                resp.getWriter().print("{\"success\": true}");
            } else {
                resp.getWriter().print("{\"success\": false, \"message\": \"Không thể cập nhật thông tin mã\"}");
            }
        } catch (Exception e) {
            resp.getWriter().print("{\"success\": false, \"message\": \"Lỗi: " + e.getMessage() + "\"}");
        }
    }
}