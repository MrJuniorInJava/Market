package ru.kolpakov.Market.models;

import java.util.List;

public class Product {
    private int id;
    private long price;
    private String name;
    private String description;
    private List<Review> reviews;
    private List<Property> properties;
}
