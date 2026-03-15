package com.webgiadung.webgiadung.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Discounts implements Serializable {
    private int id; // id mgg
    private String name; // tên mã giảm
    private String discountType; // loại giảm: theo % or theo giá tiền
    private double discountValue; // giá trị giảm, biến này phụ thuộc vào loại giảm
    private String description; // mô tả chương trình
    private LocalDateTime startDate; // ngày bắt đầu
    private LocalDateTime endDate; // ngày kết thúc
    private int categoryId; // id danh mục được giảm
    private int status; // tắt - bật

    public Discounts() {}

    public Discounts(int id, String name, String discountType, double discountValue, String description, LocalDateTime endDate, LocalDateTime startDate, int categoryId, int status) {
        this.id = id;
        this.name = name;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.description = description;
        this.endDate = endDate;
        this.startDate = startDate;
        this.categoryId = categoryId;
        this.status = status;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Discounts{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", discountType='" + discountType + '\'' +
                ", discountValue=" + discountValue +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", categoryId=" + categoryId +
                ", status=" + status +
                '}';
    }
}
