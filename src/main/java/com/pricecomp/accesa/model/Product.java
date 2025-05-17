package com.pricecomp.accesa.model;
import lombok.Data; // Lombok library to reduce boilerplate code

@Data
public class Product {
  private String productId;
  private String productName;
  private String productCategory;
  private String brand;
  private double packageQuantity;
  private String packageUnit;
  private double price;
  private String currency;

  private String  store; // Store name added manually
}
