package com.pricecomp.accesa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // This annotation generates a no-args constructor
@AllArgsConstructor // This annotation generates a constructor with all fields as parameters
/**
 * Represents a product with a discount.
 * It contains information about the product's ID, name, brand, package quantity,
 * package unit, category, discount percentage, and the date range of the discount.
 */
public class DiscountedProduct {
  private String productId;
  private String productName;
  private String brand;
  private double packageQuantity;
  private String packageUnit;
  private String productCategory;
  private String fromDate;
  private String toDate;
  private double discountPercentage;

  private String store; // Added from filename
}
