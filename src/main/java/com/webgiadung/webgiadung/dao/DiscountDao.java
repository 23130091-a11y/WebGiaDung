package com.webgiadung.webgiadung.dao;

import com.webgiadung.webgiadung.model.Discounts;

import java.util.List;

public class DiscountDao extends BaseDao {

    public int insertDiscount(Discounts d) {
        return get().withHandle(handle ->
                handle.createUpdate("""
                INSERT INTO discounts (name, discount_type, discount_value, description, start_date, end_date, category_id, status) 
                VALUES (:name, :discountType, :discountValue, :description, :startDate, :endDate, :categoryId, :status)
                """)
                        .bindBean(d)
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class)
                        .one()
        );
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
        return get().withHandle(handle ->
                handle.createUpdate("""
                UPDATE discounts 
                SET name = :name, 
                    discount_type = :discountType, 
                    discount_value = :discountValue, 
                    description = :description, 
                    start_date = :startDate, 
                    end_date = :endDate,
                    category_id = :categoryId,
                    status = :status
                WHERE id = :id
                """)
                        .bindBean(d)
                        .execute() > 0
        );
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
