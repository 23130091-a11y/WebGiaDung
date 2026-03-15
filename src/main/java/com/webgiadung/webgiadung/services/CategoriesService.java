package com.webgiadung.webgiadung.services;

import com.webgiadung.webgiadung.dao.CategoriesDao;
import com.webgiadung.webgiadung.model.Categories;

import java.util.List;

public class CategoriesService {
    CategoriesDao categoriesDao = new CategoriesDao();

    public Categories getCategory(int id) {
        return categoriesDao.getCategory(id);
    }

    public List<Categories> getAllCategories() {
        return categoriesDao.getAllCategories();
    }

    public List<Categories> getCategoriesParent() {
        return categoriesDao.getCategoriesParent();
    }

    public List<Categories> getCategoriesByParentId(int parentId) {
        return categoriesDao.getCategoriesByParentId(parentId);
    }

    public int insertCategory(String name, String description) {
        Categories exist = categoriesDao.findByName(name);

        if (exist != null) {
            return 0;
        }

        return categoriesDao.insertCategory(name, description);
    }
}