package com.webgiadung.webgiadung.dao;

import com.webgiadung.webgiadung.model.UserAddress;

import java.util.List;
import java.util.Optional;

public class UserAddressDao extends BaseDao {

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
                        .mapToBean(UserAddress.class)
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

    public int insert(UserAddress addr) {
        if (addr.getIsDefault() == 1) clearDefault(addr.getUserId());

        return get().withHandle(h ->
                h.createUpdate("""
                        INSERT INTO user_addresses(user_id, full_name, phone, address, is_default)
                        VALUES(:uid, :fn, :ph, :addr, :def)
                        """)
                        .bindBean(addr)
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(int.class)
                        .one()
        );
    }

    // hàm này chưa làm gì
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

    // thêm hàm update
    public int update(UserAddress addr) {
        if (addr.getIsDefault() == 1) {
            clearDefault(addr.getUserId());
        }
        return get().withHandle(h ->
                h.createUpdate("""
                    UPDATE user_addresses 
                    SET full_name = :fullName, phone = :phone, address = :address, is_default = :isDefault
                    WHERE id = :id AND user_id = :userId
                """)
                        .bindBean(addr)
                        .execute()
        );
    }
}
