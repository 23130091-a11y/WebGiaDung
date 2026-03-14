package com.webgiadung.doanweb.dao;

import com.webgiadung.doanweb.model.ProductDescriptions;
import java.util.List;

public class ProductDescriptionsDao extends BaseDao {

    // Thay đổi từ void sang int để trả về ID vừa sinh ra
    public int insert(ProductDescriptions desc) {
        return get().withHandle(h ->
                h.createUpdate(

                                "INSERT INTO products_description (title, description, products_id, created_at, updated_at) " +
                                        "VALUES (:title, :description, :productId, NOW(), NOW())"
                        )
                        .bindBean(desc)
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public List<ProductDescriptions> findByProductId(int productId) {
        return get().withHandle(h ->
                h.createQuery("SELECT * FROM product_descriptions WHERE product_id = :productId")
                        .bind("productId", productId)
                        .mapToBean(ProductDescriptions.class)
                        .list()
        );
    }
    public boolean update(ProductDescriptions desc) {
        return get().withHandle(h ->
                h.createUpdate("UPDATE products_description " +
                                "SET title = :title, description = :description, updated_at = NOW() " +
                                "WHERE id = :id")
                        .bind("id", desc.getId())
                        .bind("title", desc.getTitle())
                        .bind("description", desc.getDescription())
                        // Không update cột products_id vì mô tả hiếm khi chuyển từ sản phẩm này sang sản phẩm khác
                        .execute() > 0
        );
    }

    // 2. Xóa mô tả theo ID (Delete single item)
    // Dùng khi người dùng bấm nút "Xóa dòng này" trên giao diện sửa
    public boolean delete(int id) {
        return get().withHandle(h ->
                h.createUpdate("DELETE FROM products_description WHERE id = :id")
                        .bind("id", id)
                        .execute() > 0
        );
    }

    // 3. (Tùy chọn) Xóa tất cả mô tả của một sản phẩm
    // Dùng khi xóa hoàn toàn sản phẩm khỏi hệ thống
    public boolean deleteByProductId(int productId) {
        return get().withHandle(h ->
                h.createUpdate("DELETE FROM products_description WHERE products_id = :productId")
                        .bind("productId", productId)
                        .execute() > 0
        );
    }
}
