package com.pricecomp.accesa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Represents a product with normalized price per unit for recommendation purposes.
 */
@Data
@NoArgsConstructor //Generates a no-argument constructor (empty constructor)
@AllArgsConstructor //Generates a constructor with ALL fields as parameters
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

// The @Data annotation from Lombok generates getters and setters for all fields
// public String getProductName() {
//   return productName;
// }

// public void setProductName(String productName){
//   this.productName= productName;
// }