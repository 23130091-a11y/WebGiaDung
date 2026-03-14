package com.webgiadung.doanweb.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class OrderAdmin implements Serializable {
    private int id;                  // Mã đơn
    private int user_id;             // Mã khách
    private String customer_name;    // Tên khách
    private int status_transport;    // Trạng thái đơn hàng (0=Mới,1=Hoàn thành,2=Hủy)
    private int status_payment;      // Trạng thái thanh toán (0=Chưa thanh toán,1=Đã thanh toán)
    private Timestamp created_at;    // Ngày tạo
    private double total_price;      // Tổng tiền đơn

    public OrderAdmin(int id, int user_id, String customer_name, int status_transport, int status_payment, Timestamp created_at, double total_price) {
        this.id = id;
        this.user_id = user_id;
        this.customer_name = customer_name;
        this.status_transport = status_transport;
        this.status_payment = status_payment;
        this.created_at = created_at;
        this.total_price = total_price;
    }

    public OrderAdmin() {}

    @Override
    public String toString() {
        return "OrderAdmin{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", customer_name='" + customer_name + '\'' +
                ", status_transport=" + status_transport +
                ", status_payment=" + status_payment +
                ", created_at=" + created_at +
                ", total_price=" + total_price +
                '}';
    }

    // --- Thêm getter hiển thị text & class theo BEM ---
    // row
    public String getRowClass() {
        return status_transport == 2 ? "order-table__row--cancelled" : "";
    }

    //Text
    public String getStatusTransportText() {
        return switch (status_transport) {
            case 0 -> "Đơn hàng mới";
            case 1 -> "Hoàn thành";
            case 2 -> "Hủy đơn hàng";
            default -> "Không xác định";
        };
    }
    //Class
    public String getStatusTransportClass() {
        return switch (status_transport) {
            case 1 -> "order-table__status--completed";
            case 2 -> "order-table__status--cancelled";
            default -> "Không xác định";
        };
    }
    //Text
    public String getStatusPaymentText() {
        return status_payment == 0 ? "Chưa thanh toán" : "Đã thanh toán";
    }
    //Class
    public String getStatusPaymentClass() {
        return status_payment == 0 ? "order-table__payment--pending" : "order-table__payment--paid";
    }


    // Getter & Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public int getStatus_transport() {
        return status_transport;
    }

    public void setStatus_transport(int status_transport) {
        this.status_transport = status_transport;
    }

    public int getStatus_payment() {
        return status_payment;
    }

    public void setStatus_payment(int status_payment) {
        this.status_payment = status_payment;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }
}
