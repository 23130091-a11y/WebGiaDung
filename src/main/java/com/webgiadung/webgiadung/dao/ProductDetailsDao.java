package com.webgiadung.doanweb.dao;

import com.webgiadung.doanweb.model.ProductDetails;
import java.util.List;

public class ProductDetailsDao extends BaseDao {

    // Chuyển sang kiểu int để trả về ID vừa insert
    public int insert(ProductDetails detail) {
        return get().withHandle(h ->
                h.createUpdate(
                                "INSERT INTO products_detail (image, title, description, products_id, created_at, updated_at) " +
                                        "VALUES (:image, :title, :description, :productId, NOW(), NOW())"
                        )
                        .bindBean(detail) // Tự động map :image, :title, :description, :productId
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class)
                        .one()
        );
    }

    // Các hàm khác giữ nguyên
    public List<ProductDetails> findByProductId(int productId) {
        return get().withHandle(h ->
                h.createQuery("SELECT * FROM product_details WHERE id = :id ORDER BY id ASC")
                        .bind("id", productId)
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
                h.createUpdate("UPDATE products_detail " +
                                "SET title = :title, description = :description, image = :image, updated_at = NOW() " +
                                "WHERE id = :id")
                        .bind("id", detail.getId())
                        .bind("title", detail.getTitle())
                        .bind("description", detail.getDescription())
                        .bind("image", detail.getImage())
                        .execute() > 0
        );
    }

    // 3. Delete (Xóa một chi tiết cụ thể theo ID)
    // Dùng cho trường hợp user bấm nút xóa 1 dòng chi tiết
    public boolean delete(int id) {
        return get().withHandle(h ->
                h.createUpdate("DELETE FROM products_detail WHERE id = :id")
                        .bind("id", id)
                        .execute() > 0
        );
    }

}