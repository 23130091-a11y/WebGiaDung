package com.webgiadung.doanweb.dao;

import java.util.List;

public class CartItemDao extends BaseDao {

    public void addOrInc(int cartId, int productId, int qty) {
        final int q = (qty <= 0) ? 1 : qty;

        get().useHandle(h ->
                h.createUpdate("""
                    INSERT INTO cart_items(cart_id, product_id, quantity)
                    VALUES(:cid, :pid, :qty)
                    ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)
                """)
                        .bind("cid", cartId)
                        .bind("pid", productId)
                        .bind("qty", q)
                        .execute()
        );
    }

    public void setQuantity(int cartId, int productId, int qty) {
        final int q = qty;

        if (q <= 0) {
            deleteItem(cartId, productId);
            return;
        }

        get().useHandle(h ->
                h.createUpdate("""
                    INSERT INTO cart_items(cart_id, product_id, quantity)
                    VALUES(:cid, :pid, :qty)
                    ON DUPLICATE KEY UPDATE quantity = :qty
                """)
                        .bind("cid", cartId)
                        .bind("pid", productId)
                        .bind("qty", q)
                        .execute()
        );
    }

    public void deleteItem(int cartId, int productId) {
        get().useHandle(h ->
                h.createUpdate("DELETE FROM cart_items WHERE cart_id = :cid AND product_id = :pid")
                        .bind("cid", cartId)
                        .bind("pid", productId)
                        .execute()
        );
    }

    public List<Row> findItems(int cartId) {
        return get().withHandle(h ->
                h.createQuery("""
                    SELECT product_id, quantity
                    FROM cart_items
                    WHERE cart_id = :cid
                """)
                        .bind("cid", cartId)
                        .map((rs, ctx) -> new Row(rs.getInt("product_id"), rs.getInt("quantity")))
                        .list()
        );
    }

    public static class Row {
        public final int productId;
        public final int quantity;

        public Row(int productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
    }
    public void clearCartItems(int cartId) {
        get().useHandle(h ->
                h.createUpdate("DELETE FROM cart_items WHERE cart_id = :cid")
                        .bind("cid", cartId)
                        .execute()
        );
    }

}
