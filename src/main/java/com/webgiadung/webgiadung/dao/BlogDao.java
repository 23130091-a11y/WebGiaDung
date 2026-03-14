package com.webgiadung.doanweb.dao;

import com.webgiadung.doanweb.model.Blog;

import java.util.List;

public class BlogDao extends BaseDao {

    public List<Blog> findLatest(int limit) {
        int lim = (limit <= 0) ? 4 : limit;

        return get().withHandle(h ->
                h.createQuery(
                                "SELECT id, title, slug, thumbnail, summary, content, status, " +
                                        "created_at AS createdAt " +
                                        "FROM blogs " +
                                        "WHERE status = 1 " +
                                        "ORDER BY created_at DESC " +
                                        "LIMIT :lim"
                        )
                        .bind("lim", lim)
                        .mapToBean(Blog.class)
                        .list()
        );
    }

    public Blog findById(int id) {
        return get().withHandle(h ->
                h.createQuery(
                                "SELECT id, title, slug, thumbnail, summary, content, status, " +
                                        "created_at AS createdAt " +
                                        "FROM blogs WHERE id = :id"
                        )
                        .bind("id", id)
                        .mapToBean(Blog.class)
                        .findOne()
                        .orElse(null)
        );
    }
}
