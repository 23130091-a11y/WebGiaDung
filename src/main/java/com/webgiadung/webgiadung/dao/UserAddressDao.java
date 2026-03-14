package com.webgiadung.doanweb.dao;

import com.webgiadung.doanweb.model.UserAddress;

import java.util.List;
import java.util.Optional;

public class UserAddressDao extends BaseDao {

    // ✅ ALIAS để khỏi lỗi "cannot find symbol findByUser(int)"
    public List<UserAddress> findByUser(int userId) {
        return listByUser(userId);
    }

    public List<UserAddress> listByUser(int userId) {
        return get().withHandle(h ->
                h.createQuery("""
                        SELECT id, user_id, full_name, phone, address, is_default
                        FROM user_addresses
                        WHERE user_id = :uid
                        ORDER BY is_default DESC, id DESC
                        """)
                        .bind("uid", userId)
                        .map((rs, ctx) -> {
                            UserAddress a = new UserAddress();
                            a.setId(rs.getInt("id"));
                            a.setUserId(rs.getInt("user_id"));
                            a.setFullName(rs.getString("full_name"));
                            a.setPhone(rs.getString("phone"));
                            a.setAddress(rs.getString("address"));
                            a.setIsDefault(rs.getInt("is_default"));
                            return a;
                        })
                        .list()
        );
    }

    public Optional<UserAddress> findDefault(int userId) {
        return get().withHandle(h ->
                h.createQuery("""
                        SELECT id, user_id, full_name, phone, address, is_default
                        FROM user_addresses
                        WHERE user_id = :uid AND is_default = 1
                        LIMIT 1
                        """)
                        .bind("uid", userId)
                        .map((rs, ctx) -> {
                            UserAddress a = new UserAddress();
                            a.setId(rs.getInt("id"));
                            a.setUserId(rs.getInt("user_id"));
                            a.setFullName(rs.getString("full_name"));
                            a.setPhone(rs.getString("phone"));
                            a.setAddress(rs.getString("address"));
                            a.setIsDefault(rs.getInt("is_default"));
                            return a;
                        })
                        .findOne()
        );
    }

    public Optional<UserAddress> findById(int userId, int id) {
        return get().withHandle(h ->
                h.createQuery("""
                        SELECT id, user_id, full_name, phone, address, is_default
                        FROM user_addresses
                        WHERE user_id = :uid AND id = :id
                        LIMIT 1
                        """)
                        .bind("uid", userId)
                        .bind("id", id)
                        .map((rs, ctx) -> {
                            UserAddress a = new UserAddress();
                            a.setId(rs.getInt("id"));
                            a.setUserId(rs.getInt("user_id"));
                            a.setFullName(rs.getString("full_name"));
                            a.setPhone(rs.getString("phone"));
                            a.setAddress(rs.getString("address"));
                            a.setIsDefault(rs.getInt("is_default"));
                            return a;
                        })
                        .findOne()
        );
    }

    public void clearDefault(int userId) {
        get().useHandle(h ->
                h.createUpdate("UPDATE user_addresses SET is_default = 0 WHERE user_id = :uid")
                        .bind("uid", userId)
                        .execute()
        );
    }

    public int insert(int userId, String fullName, String phone, String address, int isDefault) {
        if (isDefault == 1) clearDefault(userId);

        return get().withHandle(h ->
                h.createUpdate("""
                        INSERT INTO user_addresses(user_id, full_name, phone, address, is_default)
                        VALUES(:uid, :fn, :ph, :addr, :def)
                        """)
                        .bind("uid", userId)
                        .bind("fn", fullName)
                        .bind("ph", phone)
                        .bind("addr", address)
                        .bind("def", isDefault)
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(int.class)
                        .one()
        );
    }

    public void setDefault(int userId, int addressId) {
        clearDefault(userId);
        get().useHandle(h ->
                h.createUpdate("""
                        UPDATE user_addresses
                        SET is_default = 1
                        WHERE user_id = :uid AND id = :id
                        """)
                        .bind("uid", userId)
                        .bind("id", addressId)
                        .execute()
        );
    }
    public int delete(int userId, int id) {
        return get().withHandle(h ->
                h.createUpdate("""
                    DELETE FROM user_addresses
                    WHERE user_id = :uid AND id = :id
                    """)
                        .bind("uid", userId)
                        .bind("id", id)
                        .execute()
        );
    }

}
