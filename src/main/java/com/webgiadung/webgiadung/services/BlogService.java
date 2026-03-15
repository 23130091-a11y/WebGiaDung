package com.webgiadung.webgiadung.services;

import com.webgiadung.webgiadung.dao.BlogDao;
import com.webgiadung.webgiadung.model.Blog;

import java.util.List;

public class BlogService {
    private final BlogDao blogDao = new BlogDao();

    public List<Blog> getHomeBlogs() {
        return blogDao.findLatest(4);
    }
}
