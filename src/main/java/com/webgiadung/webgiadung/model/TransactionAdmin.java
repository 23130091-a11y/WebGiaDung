package com.webgiadung.webgiadung.model;

import java.io.Serializable;

public class TransactionAdmin implements Serializable {
    private int id; // id bản ghi (chi tiết sp của 1 đơn)
    private int orderId; // id order
    private int productId; // id sp
    private String name; // tên sp
    private String avatar; // ảnh chính sp
    private int quantity; // sl
    private double price; // giá tại thời điểm mua
    private double totalPrice; // tổng tiền

    public TransactionAdmin(int id, int orderId, int productId, String name, String avatar, int quantity, double price, double totalPrice) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.name = name;
        this.avatar = avatar;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
    }

    public TransactionAdmin() {}

    @Override
    public String toString() {
        return "TransactionAdmin{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", productId=" + productId +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", totalPrice=" + totalPrice +
                '}';
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
