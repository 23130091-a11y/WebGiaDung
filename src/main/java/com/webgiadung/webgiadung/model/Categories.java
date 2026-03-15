package com.webgiadung.webgiadung.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Categories implements Serializable {
    private int id; // id danh mục
    private String name; // tên danh mục
    private String description; // mô tả
    private int isVisible; // 0 - ẩn, 1 - hiện
    private LocalDateTime createdAt; // ngày tạo
    private LocalDateTime updatedAt; // ngày update
    private int parentId; // id danh mục cha

    private List<Categories> children; // danh sách danh mục con

    public List<Categories> getChildren() {
        return children;
    }

    public void setChildren(List<Categories> children) {
        this.children = children;
    }

    public Categories(int id, String name, String description, int isVisible, LocalDateTime createdAt, LocalDateTime updatedAt, int parentId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isVisible = isVisible;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.parentId = parentId;
    }

    public Categories() {}

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(int isVisible) {
        this.isVisible = isVisible;
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

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "Categories{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isVisible=" + isVisible +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", parentId=" + parentId +
                ", children=" + children +
                '}';
    }

    public boolean isPostPublished() {
        if (this.isVisible == 1) {
            return true;
        } else {
            return false;
        }
    }
}
