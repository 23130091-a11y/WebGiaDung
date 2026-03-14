package com.webgiadung.doanweb.services;

import com.webgiadung.doanweb.dao.DiscountDao;
import com.webgiadung.doanweb.model.Discounts;

import java.util.List;

public class DiscountService {
    DiscountDao discountDao = new DiscountDao();

    public int insertDiscount(Discounts discount) {

        return discountDao.insertDiscount(discount);
    }
    public Discounts getDiscountById(int id) {
        return discountDao.getDiscountById(id);
    }
    public List<Discounts> getAllDiscounts() {
        return discountDao.getAllDiscounts();
    }
    public boolean updateDiscount(Discounts discount) {
        return discountDao.updateDiscount(discount);
    }
}