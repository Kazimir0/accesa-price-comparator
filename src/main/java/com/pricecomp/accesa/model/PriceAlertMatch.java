package com.pricecomp.accesa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a product that matches the user's target price alert.
 */
@Data
@AllArgsConstructor // This annotation generates a constructor with all fields as parameters
@NoArgsConstructor // This annotation generates a no-args constructor
/**
 * Represents a product that matches the user's target price alert.
 * It contains information about the product's name, brand, store, price,
 * package quantity, package unit, and the date of the match.
 */
public class PriceAlertMatch {
  private String productName;
  private String brand;
  private String store;
  private double price;
  private double packageQuantity;
  private String packageUnit;
  private String date;
}
