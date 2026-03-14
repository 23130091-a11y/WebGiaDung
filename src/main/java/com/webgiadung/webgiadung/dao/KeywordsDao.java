package com.webgiadung.doanweb.dao;

import com.webgiadung.doanweb.model.Keywords;
import java.util.List;

public class KeywordsDao extends BaseDao {

    // Lấy toàn bộ danh sách
    public List<Keywords> getAll() {
        return get().withHandle(h ->
                h.createQuery("SELECT * FROM keywords ORDER BY name ASC")
                        .mapToBean(Keywords.class)
                        .list()
        );
    }

    // HÀM INSERT TRẢ VỀ INT (ID TỰ ĐỘNG TẠO)
    public int insert(Keywords keyword) {
        return get().withHandle(h ->
                h.createUpdate("INSERT INTO keywords (name, description, created_at, updated_at) " +
                                "VALUES (:name, :description, NOW(), NOW())")
                        .bind("name", keyword.getName())
                        .bind("description", keyword.getDescription())
                        .executeAndReturnGeneratedKeys("id") // Lấy ID tự động tạo
                        .mapTo(Integer.class)
                        .one()
        );
    }

    // Kiểm tra tồn tại
    public boolean checkExists(String name) {
        return get().withHandle(h ->
                h.createQuery("SELECT COUNT(id) FROM keywords WHERE name = :name")
                        .bind("name", name)
                        .mapTo(Integer.class)
                        .one() > 0
        );
    }
    public Keywords getById(int id) {
        return get().withHandle(h ->
                h.createQuery("SELECT * FROM keywords WHERE id = :id")
                        .bind("id", id)
                        .mapToBean(Keywords.class)
                        .findOne() // Trả về Optional<Keywords>
                        .orElse(null) // Nếu không thấy thì trả về null
        );
    }

    // 2. Cập nhật Keyword
    // Trả về true nếu update thành công (có dòng bị ảnh hưởng), false nếu thất bại
    public boolean update(Keywords keyword) {
        return get().withHandle(h ->
                h.createUpdate("UPDATE keywords SET name = :name, description = :description, updated_at = NOW() WHERE id = :id")
                        .bind("id", keyword.getId())
                        .bind("name", keyword.getName())
                        .bind("description", keyword.getDescription())
                        .execute() > 0 // execute() trả về số dòng thay đổi
        );
    }

    // --- BỔ SUNG THÊM (NẾU CẦN XÓA) ---

    // 3. Xóa Keyword
    public boolean delete(int id) {
        return get().withHandle(h ->
                h.createUpdate("DELETE FROM keywords WHERE id = :id")
                        .bind("id", id)
                        .execute() > 0
        );
    }

}