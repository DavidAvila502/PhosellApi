package com.dev.phosell.sessionpackage.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public class SessionPackage {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer photo_count;
    private String benefits;

    public SessionPackage() {
    }

    public SessionPackage(UUID id, String name, String description, BigDecimal price, Integer photo_count, String benefits) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.photo_count = photo_count;
        this.benefits = benefits;
    }

    // setter

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setPhoto_count(Integer photo_count) {
        this.photo_count = photo_count;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    // getter

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getPhoto_count() {
        return photo_count;
    }

    public String getBenefits() {
        return benefits;
    }
}
