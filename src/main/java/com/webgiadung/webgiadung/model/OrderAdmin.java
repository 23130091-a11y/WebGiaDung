package com.webgiadung.webgiadung.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class OrderAdmin implements Serializable {
    private int id; // Mã đơn
    private int userId; // Mã khách
    private String customerName; // Tên khách
    private int statusTransport; // Trạng thái đơn hàng (0=Mới,1=Hoàn thành,2=Hủy)
    private int statusPayment; // Trạng thái thanh toán (0=Chưa thanh toán,1=Đã thanh toán)
    private LocalDateTime createdAt; // Ngày tạo
    private double totalPrice; // Tổng tiền đơn

    public OrderAdmin(int id, int userId, String customerName, int statusTransport, int statusPayment, LocalDateTime createdAt, double totalPrice) {
        this.id = id;
        this.userId = userId;
        this.customerName = customerName;
        this.statusTransport = statusTransport;
        this.statusPayment = statusPayment;
        this.createdAt = createdAt;
        this.totalPrice = totalPrice;
    }

    public OrderAdmin() {}

    @Override
    public String toString() {
        return "OrderAdmin{" +
                "id=" + id +
                ", user_id=" + userId +
                ", customer_name='" + customerName + '\'' +
                ", status_transport=" + statusTransport +
                ", status_payment=" + statusPayment +
                ", created_at=" + createdAt +
                ", total_price=" + totalPrice +
                '}';
    }

    // row (hủy đơn)
    public String getRowClass() {
        return statusTransport == 2 ? "order-table__row--cancelled" : "";
    }

    // Text
    public String getStatusTransportText() {
        return switch (statusTransport) {
            case 0 -> "Đơn hàng mới";
            case 1 -> "Hoàn thành";
            case 2 -> "Hủy đơn hàng";
            default -> "Không xác định";
        };
    }

    // Class
    public String getStatusTransportClass() {
        return switch (statusTransport) {
            case 1 -> "order-table__status--completed";
            case 2 -> "order-table__status--cancelled";
            default -> "Không xác định";
        };
    }

    // Text
    public String getStatusPaymentText() {
        return statusPayment == 0 ? "Chưa thanh toán" : "Đã thanh toán";
    }

    // Class
    public String getStatusPaymentClass() {
        return statusPayment == 0 ? "order-table__payment--pending" : "order-table__payment--paid";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getStatusTransport() {
        return statusTransport;
    }

    public void setStatusTransport(int statusTransport) {
        this.statusTransport = statusTransport;
    }

    public int getStatusPayment() {
        return statusPayment;
    }

    public void setStatusPayment(int statusPayment) {
        this.statusPayment = statusPayment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
