package com.webgiadung.doanweb.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ProductDetails implements Serializable {
    private int id;
    private String image;       // Lưu đường dẫn ảnh hoặc tên file (URL/path)
    private String title;
    private String description;
    private int productId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductDetails() {
    }

    public ProductDetails(int id, String image, String title, String description, int productId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.description = description;
        this.productId = productId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return "ProductDetails{" +
                "id=" + id +
                ", image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", productId=" + productId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}