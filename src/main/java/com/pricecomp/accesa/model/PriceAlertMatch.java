package com.pricecomp.accesa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a product that matches the user's target price alert.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceAlertMatch {
  private String productName;
  private String brand;
  private String store;
  private double price;
  private double packageQuantity;
  private String packageUnit;
  private String date;
}
