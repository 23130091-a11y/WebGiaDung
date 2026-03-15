package com.webgiadung.webgiadung.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Slide implements Serializable {
    private int id; // id của slide
    private String title; // tiêu đề của slide
    private String banner; // link ảnh của slide
    private String description; // mô tả
    private int status; // trạng thái // 0 - chưa post, 1 - đã post
    private LocalDateTime createdAt; // ngày thêm slide
    private LocalDateTime updatedAt; // ngày cập nhật gần nhất

    public Slide() {}

    public Slide(int id, String title, String banner, String description, int status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.banner = banner;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
        return "Slide{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", banner='" + banner + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}