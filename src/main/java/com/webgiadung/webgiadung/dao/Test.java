package com.webgiadung.doanweb.dao;

import com.webgiadung.doanweb.model.Product;
import com.webgiadung.doanweb.services.ProductService;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        ProductService ps = new ProductService();

        List<Product> products = ps.getListProduct();

        for (Product p : products) {
            System.out.println(p.toString());
        }
    }
}
