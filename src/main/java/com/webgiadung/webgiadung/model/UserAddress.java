package com.webgiadung.webgiadung.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UserAddress implements Serializable {
    private int id; // id
    private int userId; // id user
    private String fullName; // họ tên kh
    private String phone; // số điện thoại
    private String address; // địa chỉ cần tách ra tỉnh / huyện / xã
    private int isDefault; // 0 - ko là địa chỉ mặc định, 1 - là địa chỉ mặc định
    private LocalDateTime createdAt; // ngày thêm địa chỉ

    public UserAddress() {}

    public UserAddress(int id, int userId, String fullName, String phone, String address, int isDefault, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.isDefault = isDefault;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    public String getFullName() { return fullName; }

    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public int getIsDefault() { return isDefault; }

    public void setIsDefault(int isDefault) { this.isDefault = isDefault; }

    @Override
    public String toString() {
        return "UserAddress{" +
                "id=" + id +
                ", userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", isDefault=" + isDefault +
                ", createdAt=" + createdAt +
                '}';
    }
}
