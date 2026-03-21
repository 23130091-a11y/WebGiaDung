package com.webgiadung.webgiadung.dao;

import com.webgiadung.webgiadung.model.Blog;

import java.util.List;

public class BlogDao extends BaseDao {
    // Xem lại xử lý blog sau
    public List<Blog> findLatest(int limit) {
        // hiện 4 bài gần đây
        int lim = (limit <= 0) ? 4 : limit;

        return get().withHandle(h ->
                h.createQuery("""
                        SELECT id, title, slug, thumbnail, summary, content, status, 
                               created_at AS createdAt, 
                               updated_at AS updatedAt 
                        FROM blogs 
                        WHERE status = 1 
                        ORDER BY created_at DESC 
                        LIMIT :lim
                        """)
                        .bind("lim", lim)
                        .mapToBean(Blog.class)
                        .list()
        );
    }

    public Blog findById(int id) {
        return get().withHandle(h ->
                h.createQuery("""
                        SELECT id, title, slug, thumbnail, summary, content, status, 
                               created_at AS createdAt, 
                               updated_at AS updatedAt 
                        FROM blogs 
                        WHERE id = :id
                        """)
                        .bind("id", id)
                        .mapToBean(Blog.class)
                        .findOne()
                        .orElse(null)
        );
    }

    // thêm hàm update dùng sau
    public boolean updateBlog(Blog blog) {
        return get().withHandle(h ->
                h.createUpdate("""
                    UPDATE blogs 
                    SET title = :title, slug = :slug, thumbnail = :thumbnail, 
                        summary = :summary, content = :content, status = :status, 
                        updated_at = NOW() 
                    WHERE id = :id
                """)
                        .bindBean(blog)
                        .execute() > 0
        );
    }
}
