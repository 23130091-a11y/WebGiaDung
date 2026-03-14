package com.webgiadung.doanweb.dao;

import com.webgiadung.doanweb.model.ProductReview;

import java.sql.Timestamp;
import java.util.List;

public class ProductReviewDao extends BaseDao {

    public void insert(int productId, int userId, double rating, String comment) {
        get().useHandle(h ->
                h.createUpdate("""
                    INSERT INTO product_reviews (product_id, user_id, rating, comment, created_at)
                    VALUES (:productId, :userId, :rating, :comment, NOW())
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
                    SELECT pr.id,
                           pr.product_id,
                           pr.user_id,
                           pr.rating,
                           pr.comment,
                           pr.created_at,
                           COALESCE(u.name, u.email) AS author_name
                    FROM product_reviews pr
                    LEFT JOIN users u ON u.id = pr.user_id
                    WHERE pr.product_id = :pid
                    ORDER BY pr.created_at DESC
                """)
                        .bind("pid", productId)
                        .map((rs, ctx) -> {
                            ProductReview r = new ProductReview();
                            r.setId(rs.getInt("id"));
                            r.setProductId(rs.getInt("product_id"));
                            r.setUserId(rs.getInt("user_id"));
                            r.setRating(rs.getDouble("rating"));
                            r.setComment(rs.getString("comment"));

                            Timestamp ts = rs.getTimestamp("created_at");
                            if (ts != null) r.setCreatedAt(ts.toLocalDateTime());

                            r.setAuthorName(rs.getString("author_name"));
                            return r;
                        })
                        .list()
        );
    }
}
