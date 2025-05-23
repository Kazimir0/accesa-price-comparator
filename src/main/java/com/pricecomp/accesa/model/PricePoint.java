package com.pricecomp.accesa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single point in the price history (used for trend graphs)
 */
@Data // This annotation generates getters and setters for all fields
// Is a Lombok annotation that generates boilerplate code like getters, setters, equals, hashCode, and toString methods
@NoArgsConstructor
@AllArgsConstructor
public class PricePoint {
  private String date; // The day that discount was valid
  private double discountPercentage; // Discount percentage
}
