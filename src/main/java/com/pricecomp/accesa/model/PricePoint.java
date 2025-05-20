package com.pricecomp.accesa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single point in the price history (used for trend graphs)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PricePoint {
  private String date; // The day that discount was valid
  private double discountPercentage; // Discount percentage
}
