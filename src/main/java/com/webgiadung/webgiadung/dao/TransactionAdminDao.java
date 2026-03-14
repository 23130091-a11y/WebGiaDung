package com.webgiadung.doanweb.dao;

import com.webgiadung.doanweb.model.TransactionAdmin;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class TransactionAdminDao extends BaseDao {
    private Jdbi jdbi;

    public  TransactionAdminDao() {
        this.jdbi = get();
    }

    // Lấy chi tiết sản phẩm theo orderId
    public List<TransactionAdmin> getTransactionsByOrderId(int orderId) {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT id, order_id, product_id, name, avatar, quantity, price, total_price " +
                                        "FROM transactions " +
                                        "WHERE order_id = :orderId"
                        )
                        .bind("orderId", orderId)
                        .mapToBean(TransactionAdmin.class)
                        .list()
        );
    }
}
