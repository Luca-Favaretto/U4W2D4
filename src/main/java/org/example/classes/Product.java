package org.example.classes;

import java.util.Random;

public class Product {
    private Long id;
    private String name;
    private String category;
    private Double price;

    public Product(String name, String category, Double price) {
        Random rdn = new Random();
        this.id = rdn.nextLong();
        this.name = name;
        this.category = category;
        this.price = price;
    }


    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}
