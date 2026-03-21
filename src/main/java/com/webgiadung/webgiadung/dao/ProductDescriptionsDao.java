package com.webgiadung.webgiadung.dao;

import com.webgiadung.webgiadung.model.ProductDescriptions;
import java.util.List;

public class ProductDescriptionsDao extends BaseDao {

    // insert dòng mô tả, trả về id vừa thêm
    public int insert(ProductDescriptions desc) {
        return get().withHandle(h ->
                h.createUpdate(

                                "INSERT INTO product_descriptions (attr_name, value, product_id, created_at, updated_at) " +
                                        "VALUES (:attrName, :value, :productId, NOW(), NOW())"
                        )
                        .bindBean(desc)
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public List<ProductDescriptions> findByProductId(int productId) {
        return get().withHandle(h ->
                h.createQuery("SELECT * FROM product_descriptions WHERE product_id = :productId ORDER BY id ASC")
                        .bind("productId", productId)
                        .mapToBean(ProductDescriptions.class)
                        .list()
        );
    }

    public boolean update(ProductDescriptions desc) {
        return get().withHandle(h ->
                h.createUpdate("UPDATE product_descriptions " +
                                "SET attr_name = :attrName, value = :value, updated_at = NOW() " +
                                "WHERE id = :id")
                        .bindBean(desc)
                        .execute() > 0
        );
    }

    // Xóa mô tả theo ID
    // Dùng khi người dùng bấm nút "Xóa dòng này" trên giao diện sửa
    public boolean delete(int id) {
        return get().withHandle(h ->
                h.createUpdate("DELETE FROM product_descriptions WHERE id = :id")
                        .bind("id", id)
                        .execute() > 0
        );
    }

    // Xóa tất cả mô tả của một sản phẩm
    // Dùng khi xóa hoàn toàn sản phẩm khỏi hệ thống
    public boolean deleteByProductId(int productId) {
        return get().withHandle(h ->
                h.createUpdate("DELETE FROM product_descriptions WHERE product_id = :productId")
                        .bind("productId", productId)
                        .execute() > 0
        );
    }
}
