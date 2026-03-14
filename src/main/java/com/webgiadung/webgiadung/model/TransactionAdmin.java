package com.webgiadung.doanweb.model;

import java.io.Serializable;

public class TransactionAdmin implements Serializable {
    private int id;             // id bản ghi transaction
    private int order_id;       // id đơn hàng
    private int product_id;     // id sản phẩm
    private String name;        // tên sản phẩm
    private String avatar;      // ảnh sản phẩm
    private int quantity;       // số lượng
    private double price;       // giá cuối cùng lúc mua
    private double total_price; // price * quantity

    public TransactionAdmin(int id, int order_id, int product_id, String name, String avatar, int quantity, double price, double total_price) {
        this.id = id;
        this.order_id = order_id;
        this.product_id = product_id;
        this.name = name;
        this.avatar = avatar;
        this.quantity = quantity;
        this.price = price;
        this.total_price = total_price;
    }

    public TransactionAdmin() {}

    @Override
    public String toString() {
        return "TransactionAdmin{" +
                "id=" + id +
                ", order_id=" + order_id +
                ", product_id=" + product_id +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", total_price=" + total_price +
                '}';
    }

    // Getter & Setter
    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
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

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
