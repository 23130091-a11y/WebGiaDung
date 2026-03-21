package com.webgiadung.webgiadung.dao;

import com.webgiadung.webgiadung.model.ProductReview;

import java.sql.Timestamp;
import java.util.List;

public class ProductReviewDao extends BaseDao {

    public void insert(int productId, int userId, double rating, String comment) {
        get().useHandle(h ->
                h.createUpdate("""
                    INSERT INTO product_reviews (product_id, user_id, rating, comment, status)
                    VALUES (:productId, :userId, :rating, :comment, 1)
                """)
                        .bind("productId", productId)
                        .bind("userId", userId)
                        .bind("rating", rating)
                        .bind("comment", comment)
                        .execute()
        );
    }

    public List<ProductReview> findByProductId(int productId) {
        return get().withHandle(h ->
                h.createQuery("""
                    SELECT pr.*, 
                           COALESCE(u.name, u.email) AS authorName
                    FROM product_reviews pr
                    LEFT JOIN users u ON u.id = pr.user_id
                    WHERE pr.product_id = :pid AND pr.status = 1
                    ORDER BY pr.created_at DESC
                """)
                        .bind("pid", productId)
                        .mapToBean(ProductReview.class)
                        .list()
        );
    }

    // hàm này dùng sau trong admin, xem lại tất cả review kể cả cái bị ẩn
    public List<ProductReview> findAllForAdmin() {
        return get().withHandle(h ->
                h.createQuery("SELECT pr.*, u.name AS authorName FROM product_reviews pr JOIN users u ON u.id = pr.user_id ORDER BY pr.created_at DESC")
                        .mapToBean(ProductReview.class)
                        .list()
        );
    }
}
