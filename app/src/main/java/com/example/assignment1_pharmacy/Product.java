package com.example.assignment1_pharmacy;
public class Product {
    private int id;
    private String name;
    private double price;
    private int stock;
    private int imageResId;

    public Product(int id, String name, double price, int stock, int imageResId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.imageResId = imageResId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public int getImageResId() { return imageResId; }

    public void setStock(int stock) { this.stock = stock; }
    // Override equals and hashCode for correct comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return id == product.id;  // Compare products based on the ID or other fields
    }
}