package com.webgiadung.doanweb.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Blog {
    private int id;
    private String title;
    private String slug;
    private String thumbnail;
    private String summary;
    private String content;
    private int status;

    // CHỈ GIỮ 1 createdAt kiểu LocalDateTime
    private LocalDateTime createdAt;

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

    // Getter/Setter chuẩn cho JDBI map vào
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Getter PHỤ cho JSP fmt:formatDate
    public Date getCreatedAtDate() {
        if (createdAt == null) return null;
        return Date.from(createdAt.atZone(ZoneId.systemDefault()).toInstant());
    }
}
