package com.webgiadung.doanweb.dao;

import com.webgiadung.doanweb.model.OrderAdmin;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class OrderAdminDao extends BaseDao {
    private Jdbi jdbi;

    public OrderAdminDao() {
        this.jdbi = get();
    }

    // Lấy tất cả đơn hàng
    public List<OrderAdmin> getAllOrders() {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                        "SELECT o.id, u.name AS customer_name, " +
                                "o.status_transport, o.status_payment, o.created_at, o.total_price " +
                                "FROM orders o " +
                                "JOIN users u ON o.user_id = u.id " +
                                "ORDER BY o.created_at DESC"
                ).mapToBean(OrderAdmin.class).list()
        );
    }

    // Tìm kiếm theo từ khóa (mã đơn hoặc tên khách)
    public List<OrderAdmin> searchOrders(String keyword) {
        // 1. Xử lý keyword ngay từ đầu, đảm bảo không bị lỗi null
        String searchPattern = "%" + (keyword == null ? "" : keyword.trim()) + "%";

        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT o.id, " +
                                        "       u.name AS customer_name, " + // Đảm bảo khớp với field 'customer_name' trong Bean
                                        "       o.status_transport, " +
                                        "       o.status_payment, " +
                                        "       o.created_at, " +
                                        "       o.total_price " +
                                        "FROM orders o " +
                                        "JOIN users u ON o.user_id = u.id " +
                                        "WHERE CAST(o.id AS CHAR) LIKE :kw " + // Dùng chung 1 tham số :kw
                                        "   OR u.name LIKE :kw " +
                                        "ORDER BY o.created_at DESC"
                        )
                        .bind("kw", searchPattern) // JDBI tự hiểu và gán vào tất cả vị trí có :kw
                        .mapToBean(OrderAdmin.class)
                        .list()
        );
    }

    // Lọc theo trạng thái
//    public List<OrderAdmin> filterOrders(Integer statusPayment, Integer statusTransport) {
//        return jdbi.withHandle(handle ->
//                handle.createQuery(
//                                "SELECT o.id, o.user_id, u.name as customer_name, " +
//                                        "o.status_transport, o.status_payment, o.created_at, o.total_price " +
//                                        "FROM orders o " +
//                                        "JOIN users u ON o.user_id = u.id " +
//                                        "WHERE (:statusPayment IS NULL OR o.status_payment = :statusPayment) " +
//                                        "AND (:statusTransport IS NULL OR o.status_transport = :statusTransport) " +
//                                        "ORDER BY o.created_at DESC"
//                        )
//                        .bind("statusPayment", statusPayment)
//                        .bind("statusTransport", statusTransport)
//                        .mapToBean(OrderAdmin.class)
//                        .list()
//        );
//    }

    // Xóa nhiều đơn hàng
    public void deleteOrders(List<Integer> orderIds) {
        if(orderIds == null || orderIds.isEmpty()) return;

        jdbi.useHandle(handle ->
                handle.createUpdate("DELETE FROM orders WHERE id IN (<ids>)")
                        .bindList("ids", orderIds)
                        .execute()
        );
    }

    public void updateStatusTransport(int orderId, int statusTransport) {
        jdbi.useHandle(handle ->
                handle.createUpdate(
                                "UPDATE orders SET status_transport = :status WHERE id = :id"
                        )
                        .bind("status", statusTransport)
                        .bind("id", orderId)
                        .execute()
        );
    }

    public void updateStatusPayment(int orderId, int statusPayment) {
        jdbi.useHandle(handle ->
                handle.createUpdate(
                                "UPDATE orders SET status_payment = :status WHERE id = :id"
                        )
                        .bind("status", statusPayment)
                        .bind("id", orderId)
                        .execute()
        );
    }
}
