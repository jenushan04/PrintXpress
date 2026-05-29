package com.printxpress.app.model;

public class Promotion {
    private long id;
    private String title;
    private String description;
    private int discountPercent;
    private String validUntil;
    private boolean active;

    public Promotion() {}

    public Promotion(long id, String title, String description, int discountPercent,
                     String validUntil, boolean active) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.discountPercent = discountPercent;
        this.validUntil = validUntil;
        this.active = active;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(int discountPercent) { this.discountPercent = discountPercent; }
    public String getValidUntil() { return validUntil; }
    public void setValidUntil(String validUntil) { this.validUntil = validUntil; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
