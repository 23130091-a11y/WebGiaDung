package com.webgiadung.webgiadung.dao;

import com.webgiadung.webgiadung.model.Discounts;

import java.util.List;

public class DiscountDao extends BaseDao {

    public int insertDiscount(Discounts d) {
        return get().withHandle(handle -> {
            return handle.createUpdate("""
            INSERT INTO discounts (name, type_discount, discount, description, start_date, end_date, id_cate) 
            VALUES (:name, :typeDiscount, :discount, :description, :startDate, :endDate, :id_cate)
            """)
                    .bindBean(d)
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(Integer.class)
                    .one();
        });
    }
    public Discounts getDiscountById(int id) {
        return get().withHandle(handle -> {
            return handle.createQuery("SELECT * FROM discounts WHERE id = :id")
                    .bind("id", id)
                    .mapToBean(Discounts.class)
                    .findOne()
                    .orElse(null);
        });
    }
    public List<Discounts> getAllDiscounts() {
        return get().withHandle(handle -> {
            return handle.createQuery("SELECT * FROM discounts ORDER BY id DESC")
                    .mapToBean(Discounts.class)
                    .list();
        });
    }
    public boolean updateDiscount(Discounts d) {
        return get().withHandle(handle -> {
            int rowsAffected = handle.createUpdate("""
            UPDATE discounts 
            SET name = :name, 
                type_discount = :typeDiscount, 
                discount = :discount, 
                description = :description, 
                start_date = :startDate, 
                end_date = :endDate 
            WHERE id = :id
            """)
                    .bindBean(d)
                    .execute();

            return rowsAffected > 0;
        });
    }
    public boolean deleteDiscount(int id) {
        return get().withHandle(handle -> {
            int rowsAffected = handle.createUpdate("DELETE FROM discounts WHERE id = :id")
                    .bind("id", id)
                    .execute();
            return rowsAffected > 0;
        });
    }
}
