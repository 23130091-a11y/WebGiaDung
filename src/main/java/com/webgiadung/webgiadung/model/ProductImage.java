package com.webgiadung.doanweb.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ProductImage implements Serializable {
    private int id;
    private String path;       // URL hoặc tên file
    private int productId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductImage() {}

    public ProductImage(int id, String path, int productId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.path = path;
        this.productId = productId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "ProductImage{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", productId=" + productId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
