package com.pricecomp.accesa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Represents a product with normalized price per unit for recommendation purposes.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedProduct {
  private String productName;
  private String brand;
  private String productCategory;
  private double packageQuantity;
  private String packageUnit;
  private double price;
  private String store; // Extracted from the filename
  private double valuePerUnit; // Calculated: price / packageQuantity
}
