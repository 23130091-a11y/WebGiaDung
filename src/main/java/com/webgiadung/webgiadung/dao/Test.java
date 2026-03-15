package com.webgiadung.webgiadung.dao;

import com.webgiadung.webgiadung.model.Product;
import com.webgiadung.webgiadung.services.ProductService;

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
