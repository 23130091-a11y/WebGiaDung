package com.webgiadung.webgiadung.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Product implements Serializable {
    private int id;
    private String name;
    private String image;
    private double firstPrice;
    private int discountsId;
    private double totalPrice;
    private int categoriesId;
    private int brandsId;
    private int keywordsId;
    private String brandName;
    private String keywordName;
    private int post;
    private int quantity;
    private int quantitySaled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Liên kết các bảng phụ
    private List<ProductDescriptions> descriptionsList;  // Thông số động
    private List<ProductDetails> detailsList;
    private List<ProductImage> images;          // Ảnh phụ
    private List<ProductReview> reviews;        // Đánh giá

    // rating tính sẵn từ SQL (dùng cho trang list)
    private Double ratingAvg;

    public Double getRatingAvg() {
        return (ratingAvg != null) ? ratingAvg : 0.0;
    }

    public void setRatingAvg(Double ratingAvg) {
        this.ratingAvg = ratingAvg;
    }

    // ===== Khuyến mãi (dùng cho trang list) =====
    private Double discountPercent;   // 25
    private String discountType;      // percentage | fixed

    public Double getDiscountPercent() {
        return discountPercent != null ? discountPercent : 0.0;
    }

    public void setDiscountPercent(Double discountPercent) {
        this.discountPercent = discountPercent;
    }
    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public double getRating() {
        if (reviews == null || reviews.isEmpty()) return 0.0;

        double avg = reviews.stream()
                .mapToDouble(ProductReview::getRating)
                .average()
                .orElse(0.0);

        return Math.round(avg * 10.0) / 10.0; // làm tròn 1 chữ số
    }

    public int getRatingInt() {
        return (int) Math.round(getRating()); // làm tròn để hiển thị sao
    }

    // Trong Product.java
    public boolean getIsDiscounted() {
        // Thêm kiểm tra null và đảm bảo giá gốc lớn hơn giá tổng ít nhất 1000đ (để tránh sai số)
        return discountPercent != null
                && discountPercent > 0
                && (firstPrice - totalPrice > 1);
    }

    public Product() {
    }

    public Product(int id, String name, String image, double firstPrice, double totalPrice, int discountsId, int categoriesId, int brandsId, int keywordsId, int post, int quantity, int quantitySaled, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.firstPrice = firstPrice;
        this.totalPrice = totalPrice;
        this.discountsId = discountsId;
        this.categoriesId = categoriesId;
        this.brandsId = brandsId;
        this.keywordsId = keywordsId;
        this.post = post;
        this.quantity = quantity;
        this.quantitySaled = quantitySaled;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.images = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }

    public Product(int id, String name, String image, double firstPrice, int discountsId, double totalPrice, int categoriesId, int brandsId, int keywordsId, String brandName, String keywordName, int post, int quantity, int quantitySaled, LocalDateTime createdAt, LocalDateTime updatedAt, List<ProductDescriptions> descriptionsList, List<ProductDetails> detailsList, List<ProductReview> reviews, Double discountPercent, String discountType) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.firstPrice = firstPrice;
        this.discountsId = discountsId;
        this.totalPrice = totalPrice;
        this.categoriesId = categoriesId;
        this.brandsId = brandsId;
        this.keywordsId = keywordsId;
        this.brandName = brandName;
        this.keywordName = keywordName;
        this.post = post;
        this.quantity = quantity;
        this.quantitySaled = quantitySaled;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.descriptionsList = descriptionsList;
        this.detailsList = detailsList;
        this.reviews = reviews;
        this.discountPercent = discountPercent;
        this.discountType = discountType;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getDiscountsId() {
        return discountsId;
    }

    public void setDiscountsId(int discountsId) {
        this.discountsId = discountsId;
    }

    public double getFirstPrice() {
        return firstPrice;
    }

    public void setFirstPrice(double firstPrice) {
        this.firstPrice = firstPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getCategoriesId() {
        return categoriesId;
    }

    public void setCategoriesId(int categoriesId) {
        this.categoriesId = categoriesId;
    }

    public int getBrandsId() {
        return brandsId;
    }

    public void setBrandsId(int brandsId) {
        this.brandsId = brandsId;
    }

    public int getKeywordsId() {
        return keywordsId;
    }

    public void setKeywordsId(int keywordsId) {
        this.keywordsId = keywordsId;
    }

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantitySaled() {
        return quantitySaled;
    }

    public void setQuantitySaled(int quantitySaled) {
        this.quantitySaled = quantitySaled;
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


    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    public List<ProductReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<ProductReview> reviews) {
        this.reviews = reviews;
    }

    public List<ProductDescriptions> getDescriptionsList() {
        return descriptionsList;
    }

    public void setDescriptionsList(List<ProductDescriptions> descriptionsList) {
        this.descriptionsList = descriptionsList;
    }

    public List<ProductDetails> getDetailsList() {
        return detailsList;
    }

    public void setDetailsList(List<ProductDetails> detailsList) {
        this.detailsList = detailsList;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getKeywordName() {
        return keywordName;
    }

    public void setKeywordName(String keywordName) {
        this.keywordName = keywordName;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", firstPrice=" + firstPrice +
                ", discountsId=" + discountsId +
                ", totalPrice=" + totalPrice +
                ", categoriesId=" + categoriesId +
                ", brandsId=" + brandsId +
                ", keywordsId=" + keywordsId +
                ", post=" + post +
                ", quantity=" + quantity +
                ", quantitySaled=" + quantitySaled +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
