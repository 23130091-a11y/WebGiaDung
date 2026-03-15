package com.webgiadung.webgiadung.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ProductReview implements Serializable {
    private int id; // id review
    private int productId; // id sản phẩm
    private int userId; // id user
    private double rating; // đánh giá 0-5 sao
    private String comment; // bình luận
    private LocalDateTime createdAt; // ngày tạo đánh giá

    public ProductReview() {}

    public ProductReview(int id, int productId, int userId, double rating, String comment, LocalDateTime createdAt) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ProductReview{" +
                "id=" + id +
                ", productId=" + productId +
                ", userId=" + userId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
