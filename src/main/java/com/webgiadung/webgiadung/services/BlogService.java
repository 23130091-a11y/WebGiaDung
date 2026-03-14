package com.webgiadung.doanweb.services;

import com.webgiadung.doanweb.dao.BlogDao;
import com.webgiadung.doanweb.model.Blog;

import java.util.List;

public class BlogService {
    private final BlogDao blogDao = new BlogDao();

    public List<Blog> getHomeBlogs() {
        return blogDao.findLatest(4);
    }
}
