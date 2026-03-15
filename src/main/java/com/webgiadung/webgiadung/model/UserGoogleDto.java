package com.webgiadung.webgiadung.model;

import java.io.Serializable;

public class UserGoogleDto implements Serializable {
    private String id; // id của user
    private String email; // email của user
    private boolean verifiedEmail; // email của user đã được GG verified hay chưa
    private String name; // tên đầy đủ của user
    private String givenName; // tên của user
    private String familyName; // họ của user
    private String picture; // url ảnh đại diện tài khoản GG

    public UserGoogleDto() {}

    public UserGoogleDto(String id, String email, boolean verifiedEmail, String name, String givenName, String familyName, String picture) {
        this.id = id;
        this.email = email;
        this.verifiedEmail = verifiedEmail;
        this.name = name;
        this.givenName = givenName;
        this.familyName = familyName;
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isVerifiedEmail() {
        return verifiedEmail;
    }

    public void setVerifiedEmail(boolean verifiedEmail) {
        this.verifiedEmail = verifiedEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "UserGoogleDto{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", verifiedEmail=" + verifiedEmail +
                ", name='" + name + '\'' +
                ", givenName='" + givenName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }
}
