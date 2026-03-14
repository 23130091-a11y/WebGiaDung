package com.webgiadung.doanweb.dao;

public class CartDao extends BaseDao {
    public int getOrCreateCartId(int userId) {
        Integer id = get().withHandle(h ->
                h.createQuery("SELECT id FROM carts WHERE user_id = :uid")
                        .bind("uid", userId)
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(null)
        );
        if (id != null) return id;

        return get().withHandle(h ->
                h.createUpdate("INSERT INTO carts(user_id) VALUES(:uid)")
                        .bind("uid", userId)
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class)
                        .one()
        );
    }
}
