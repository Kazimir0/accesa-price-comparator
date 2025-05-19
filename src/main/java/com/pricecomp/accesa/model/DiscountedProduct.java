package com.pricecomp.accesa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
