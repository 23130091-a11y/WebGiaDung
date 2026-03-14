package com.webgiadung.doanweb.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Categories implements Serializable {
    private int id;
    private String name;
    private String description;
    private int post;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int parentId;

    private List<Categories> children;

    public List<Categories> getChildren() {
        return children;
    }

    public void setChildren(List<Categories> children) {
        this.children = children;
    }

    public Categories(String name, String description, int post, int parentId) {
        this.name = name;
        this.description = description;
        this.post = post;
        this.parentId = parentId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Categories(int id, String name, String description, int post, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.post = post;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Categories() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
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
                ", post=" + post +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", parentId=" + parentId +
                '}';
    }
    public boolean isPostPublished() {
        if (this.post == 1) {
            return true;
        } else {
            return false;
        }
    }
}
