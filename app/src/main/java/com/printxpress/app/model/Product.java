package com.printxpress.app.model;

public class Product {
    private long id;
    private String name;
    private String category;
    private String description;
    private String material;
    private String sizeOption;
    private double price;
    private boolean active;

    public Product() {}

    public Product(long id, String name, String category, String description,
                   String material, String sizeOption, double price, boolean active) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.material = material;
        this.sizeOption = sizeOption;
        this.price = price;
        this.active = active;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }
    public String getSizeOption() { return sizeOption; }
    public void setSizeOption(String sizeOption) { this.sizeOption = sizeOption; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
