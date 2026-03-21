package com.webgiadung.webgiadung.dao;

import com.webgiadung.webgiadung.model.Categories;

import java.util.List;

public class CategoriesDao extends BaseDao {
    // Lấy tất cả danh mục
    public List<Categories> getAllCategories() {
        return get().withHandle(h ->
                h.createQuery("""
                SELECT *
                FROM categories
            """)
                        .mapToBean(Categories.class)
                        .list()
        );
    }

    // Lấy category theo id
    public Categories getCategory(int id) {
        return get().withHandle(h ->
                h.createQuery("""
                SELECT *
                FROM categories
                WHERE id = :id
            """)
                        .bind("id", id)
                        .mapToBean(Categories.class)
                        .findOne()
                        .orElse(null)
        );
    }

    // trả về ds các danh mục cha
    public List<Categories> getCategoriesParent() {
        return get().withHandle(h ->
                h.createQuery("SELECT * FROM categories WHERE parent_id = 0 AND is_visible = 1")
                        .mapToBean(Categories.class)
                        .list()
        );
    }

    // Lấy danh sách danh mục con theo parent_id,
    // Toán tử <=> sẽ hiểu được cả trường hợp so sánh với số và so sánh với NULL
    public List<Categories> getCategoriesByParentId(Integer parentId) {
        return get().withHandle(h ->
                h.createQuery("""
                    SELECT *
                    FROM categories
                    WHERE parent_id <=> :parentId
                """)
                        .bind("parentId", parentId)
                        .mapToBean(Categories.class)
                        .list()
        );
    }

    public Categories findByName(String name) {
        return get().withHandle(h -> {
            return h.createQuery("SELECT * FROM categories WHERE name = :name")
                    .bind("name", name)
                    .mapToBean(Categories.class)
                    .stream()
                    .findFirst()
                    .orElse(null);
        });
    }

    public int insertCategory(String name, String description, Integer parentId) {
        return get().withHandle(h ->
                h.createUpdate("""
                    INSERT INTO categories (name, description, parent_id, is_visible, created_at, updated_at) 
                    VALUES (:name, :description, :parentId, 1, NOW(), NOW())
                """)
                        .bind("name", name)
                        .bind("description", description)
                        .bind("parentId", parentId)
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class)
                        .one()
        );
    }

    // hàm này lấy cây danh mục lưu thành list
    public List<Categories> getCategoryTree() {
        List<Categories> parents = getCategoriesParent();
        for (Categories parent : parents) {
            // Chỉ lấy con của những danh mục đang hiển thị
            List<Categories> children = get().withHandle(h ->
                    h.createQuery("SELECT * FROM categories WHERE parent_id = :pid AND is_visible = 1")
                            .bind("pid", parent.getId())
                            .mapToBean(Categories.class)
                            .list()
            );
            parent.setChildren(children);
        }
        return parents;
    }

    // thêm hàm cập nhật danh mục
    public boolean updateCategory(Categories cat) {
        return get().withHandle(h ->
                h.createUpdate("""
                    UPDATE categories 
                    SET name = :name, description = :description, 
                        parent_id = :parentId, is_visible = :isVisible, updated_at = NOW() 
                    WHERE id = :id
                """)
                        .bindBean(cat)
                        .execute() > 0
        );
    }
}
