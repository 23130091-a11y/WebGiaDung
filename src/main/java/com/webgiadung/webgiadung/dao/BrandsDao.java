package com.webgiadung.doanweb.dao;

import com.webgiadung.doanweb.model.Brands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class BrandsDao extends BaseDao {

    // 1. Lấy toàn bộ danh sách thương hiệu để hiển thị trong <select>
    public List<Brands> getAll() {
        return get().withHandle(h ->
                h.createQuery("SELECT * FROM brands ORDER BY name ASC")
                        .mapToBean(Brands.class)
                        .list()
        );
    }

    // 2. Thêm mới một thương hiệu (Trả về ID vừa tạo để AJAX cập nhật UI)
    public int insert(Brands brand) {
        try {
            return get().withHandle(h ->
                    h.createUpdate("INSERT INTO brands (name, country, logo, created_at, updated_at) " +
                                    "VALUES (:name, :country, :logo, NOW(), NOW())")
                            .bind("name", brand.getName())      // Bind trực tiếp thay vì bindBean
                            .bind("country", brand.getCountry())
                            .bind("logo", brand.getLogo())
                            .executeAndReturnGeneratedKeys("id")
                            .mapTo(Integer.class)
                            .one()
            );
        } catch (Exception e) {
            e.printStackTrace(); // Xem lỗi cụ thể tại đây (ví dụ: sai tên cột)
            return -1;
        }
    }
    // 3. Tìm thương hiệu theo ID
    public Brands findById(int id) {
        return get().withHandle(h ->
                h.createQuery("SELECT * FROM brands WHERE id = :id")
                        .bind("id", id)
                        .mapToBean(Brands.class)
                        .findOne()
                        .orElse(null)
        );
    }

    // 4. Kiểm tra thương hiệu đã tồn tại chưa
    public boolean checkExists(String name) {
        return get().withHandle(h ->
                h.createQuery("SELECT COUNT(id) FROM brands WHERE name = :name")
                        .bind("name", name)
                        .mapTo(Integer.class)
                        .one() > 0
        );
    }
    public boolean update(Brands brand) {
        return get().withHandle(h ->
                // Đã xóa "logo = :logo" trong câu SQL
                h.createUpdate("UPDATE brands SET name = :name, country = :country, updated_at = NOW() WHERE id = :id")
                        .bind("id", brand.getId())
                        .bind("name", brand.getName())
                        .bind("country", brand.getCountry())
                        // Đã xóa dòng bind("logo", ...)
                        .execute() > 0
        );
    }

    // 6. Xóa thương hiệu (Giữ nguyên)
    public boolean delete(int id) {
        return get().withHandle(h ->
                h.createUpdate("DELETE FROM brands WHERE id = :id")
                        .bind("id", id)
                        .execute() > 0
        );
    }
}