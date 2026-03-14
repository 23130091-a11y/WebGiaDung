package com.webgiadung.doanweb.controller.admin;

import com.webgiadung.doanweb.dao.OrderAdminDao;
import com.webgiadung.doanweb.model.OrderAdmin;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "OrderSearchServlet", value = "/order-search")
public class OrderSearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        OrderAdminDao orderAdminDao = new OrderAdminDao();
        List<OrderAdmin> orders;

        if (keyword == null || keyword.trim().isEmpty()) {
            orders = orderAdminDao.getAllOrders();
        } else {
            orders = orderAdminDao.searchOrders(keyword.trim());
        }

        request.setAttribute("orders", orders);

        // Kiểm tra xem đây có phải là yêu cầu AJAX không
        String xRequestedWith = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(xRequestedWith)) {
            // Chỉ trả về phần bảng (tạo file jsp riêng hoặc dùng file hiện tại nhưng chỉ lấy đoạn nội dung)
            request.getRequestDispatcher("/_order_list.jsp").forward(request, response);
        } else {
            // Trả về cả trang nếu người dùng load trực tiếp bằng link
            request.setAttribute("keyword", keyword);
            request.getRequestDispatcher("/admin.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}