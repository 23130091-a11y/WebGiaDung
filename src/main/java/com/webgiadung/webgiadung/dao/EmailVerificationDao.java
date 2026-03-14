package com.webgiadung.doanweb.dao;

public class EmailVerificationDao extends BaseDao {

    public void saveToken(String email, String token) {
        get().useHandle(h ->
                h.createUpdate(
                                "INSERT INTO email_verification (email, token) VALUES (:email, :token)"
                        )
                        .bind("email", email)
                        .bind("token", token)
                        .execute()
        );
    }

    public String getEmailByToken(String token) {
        return get().withHandle(h ->
                h.createQuery(
                                "SELECT email FROM email_verification WHERE token = :token"
                        )
                        .bind("token", token)
                        .mapTo(String.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public void deleteToken(String token) {
        get().useHandle(h ->
                h.createUpdate(
                                "DELETE FROM email_verification WHERE token = :token"
                        )
                        .bind("token", token)
                        .execute()
        );
    }
}
