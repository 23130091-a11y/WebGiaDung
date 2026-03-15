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
                h.createQuery("""
                SELECT *
                FROM categories
                WHERE parent_id IS NULL OR parent_id = 0
            """)
                        .mapToBean(Categories.class)
                        .list()
        );
    }

    // Lấy danh sách danh mục con theo parent_id
    public List<Categories> getCategoriesByParentId(int parentId) {
        return get().withHandle(h ->
                h.createQuery("""
                    SELECT *
                    FROM categories
                    WHERE parent_id = :parentId
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

    public int insertCategory(String name, String description) {
        try {
            return get().withHandle(h ->
                    h.createUpdate("""
                    INSERT INTO categories (name, description,  parent_id, created_at, updated_at) 
                    VALUES (:name, :description,  0, NOW(), NOW())
                """)
                            .bind("name", name)
                            .bind("description", description)
                            .executeAndReturnGeneratedKeys("id")
                            .mapTo(Integer.class)
                            .one() // Dùng .one(): phải có đúng 1 kq -> JDBI sẽ ném Exception
            );
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<Categories> getCategoryTree() {
        List<Categories> parents = getCategoriesParent();
        for (Categories parent : parents) {
            List<Categories> children =
                    getCategoriesByParentId(parent.getId());
            parent.setChildren(children);
        }
        return parents;
    }
}
