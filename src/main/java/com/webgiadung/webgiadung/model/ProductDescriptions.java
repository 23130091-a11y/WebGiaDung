package com.webgiadung.webgiadung.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ProductDescriptions implements Serializable {
    private int id; // id mô tả
    private String attrName; // tên mô tả
    private String value; // thông tin mô tả
    private int productId; // id p
    private LocalDateTime createdAt; // ngày tạo
    private LocalDateTime updatedAt; // ngày update

    public ProductDescriptions() {}

    public ProductDescriptions(int id, String attrName, String value, int productId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.attrName = attrName;
        this.value = value;
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

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
        return "ProductDescriptions{" +
                "id=" + id +
                ", attrName='" + attrName + '\'' +
                ", value='" + value + '\'' +
                ", productId=" + productId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
