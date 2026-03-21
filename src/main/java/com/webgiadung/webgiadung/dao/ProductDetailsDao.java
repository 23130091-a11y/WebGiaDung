package com.webgiadung.webgiadung.dao;

import com.webgiadung.webgiadung.model.ProductDetails;
import java.util.List;

public class ProductDetailsDao extends BaseDao {

    // insert dòng chi tiết sp -> id đã thêm
    public int insert(ProductDetails detail) {
        return get().withHandle(h ->
                h.createUpdate(
                                "INSERT INTO product_details (image, title, description, product_id, created_at, updated_at) " +
                                        "VALUES (:image, :title, :description, :productId, NOW(), NOW())"
                        )
                        .bindBean(detail)
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public List<ProductDetails> findByProductId(int productId) {
        return get().withHandle(h ->
                h.createQuery("SELECT * FROM product_details WHERE product_id = :productId ORDER BY id ASC")
                        .bind("productId", productId)
                        .mapToBean(ProductDetails.class)
                        .list()
        );
    }

    public void deleteByProductId(int productId) {
        get().useHandle(h ->
                h.createUpdate("DELETE FROM product_details WHERE product_id = :productId")
                        .bind("productId", productId)
                        .execute()
        );
    }

    public boolean update(ProductDetails detail) {
        return get().withHandle(h ->
                h.createUpdate("UPDATE product_details " +
                                "SET title = :title, description = :description, image = :image, updated_at = NOW() " +
                                "WHERE id = :id")
                        .bindBean(detail)
                        .execute() > 0
        );
    }

    // Xóa một chi tiết cụ thể theo ID
    // Dùng cho trường hợp user bấm nút xóa 1 dòng chi tiết
    public boolean delete(int id) {
        return get().withHandle(h ->
                h.createUpdate("DELETE FROM product_details WHERE id = :id")
                        .bind("id", id)
                        .execute() > 0
        );
    }

}