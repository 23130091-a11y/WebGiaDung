package com.webgiadung.webgiadung.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Blog implements Serializable {
    private int id; // id
    private String title; // tiêu đề
    private String slug; // url seo (url ngắn gọn chứa từ khóa cần tìm)
    private String thumbnail; // ảnh bìa
    private String summary; // tóm tắt ngắn đầu bài
    private String content; // nội dung
    private int status; // 0 - ẩn, 1 - hiển thị
    private LocalDateTime createdAt; // thời gian tạo
    private LocalDateTime updatedAt; // thời gian cập nhật

    public Blog() {}

    public Blog(int id, String title, String slug, String thumbnail, String summary, String content, int status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.slug = slug;
        this.thumbnail = thumbnail;
        this.summary = summary;
        this.content = content;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getSlug() { return slug; }

    public void setSlug(String slug) { this.slug = slug; }

    public String getThumbnail() { return thumbnail; }

    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public String getSummary() { return summary; }

    public void setSummary(String summary) { this.summary = summary; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public int getStatus() { return status; }

    public void setStatus(int status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", slug='" + slug + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
