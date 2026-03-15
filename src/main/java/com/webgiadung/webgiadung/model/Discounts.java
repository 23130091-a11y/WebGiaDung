package com.webgiadung.webgiadung.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Discounts implements Serializable {
    private int id;
    private String name;
    private String typeDiscount;
    private double discount;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int id_cate;

    public Discounts() {
    }

    public Discounts(int id, String name, String typeDiscount, double discount, String description, LocalDateTime endDate, LocalDateTime startDate, int id_cate) {
        this.id = id;
        this.name = name;
        this.typeDiscount = typeDiscount;
        this.discount = discount;
        this.description = description;
        this.endDate = endDate;
        this.startDate = startDate;
        this.id_cate = id_cate;
    }

    public int getId_cate() {
        return id_cate;
    }

    public void setId_cate(int id_cate) {
        this.id_cate = id_cate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeDiscount() {
        return typeDiscount;
    }

    public void setTypeDiscount(String typeDiscount) {
        this.typeDiscount = typeDiscount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Discounts{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", typeDiscount='" + typeDiscount + '\'' +
                ", discount=" + discount +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
