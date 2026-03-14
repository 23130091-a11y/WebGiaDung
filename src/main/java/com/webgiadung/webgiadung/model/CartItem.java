package com.webgiadung.doanweb.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CartItem implements Serializable {
    private Product product;        // Thông tin sản phẩm
    private int quantity;           // Số lượng trong giỏ
    private double originalPrice;   // Giá gốc ban đầu của 1 sản phẩm
    private double discountPrice;   // Giá sau khi áp dụng khuyến mãi 1 sản phẩm
    private LocalDateTime addedAt;  // Thời gian thêm vào giỏ

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public double getOriginalPrice() { return originalPrice; }
    public double getDiscountPrice() { return discountPrice; }
    public LocalDateTime getAddedAt() { return addedAt; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public CartItem() {}

    public CartItem(Product product, int quantity, double discountPrice) {
        this.product = product;
        this.quantity = quantity <= 0 ? 1 : quantity;
        this.originalPrice = product.getFirstPrice(); // tự động lấy giá gốc
        this.discountPrice = discountPrice;           // giá sau khi áp dụng discount
        this.addedAt = LocalDateTime.now();           // set thời gian hiện tại
    }

    // Giá tổng theo số lượng
    public double getTotalPrice() {
        return discountPrice * quantity;
    }

    public void upQuantity(int quantity) {
        this.quantity += quantity;
    }
}
