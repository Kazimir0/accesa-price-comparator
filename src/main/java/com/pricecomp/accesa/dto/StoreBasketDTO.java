package com.pricecomp.accesa.dto;

import com.pricecomp.accesa.model.Product;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * This class represents a Data Transfer Object (DTO) for a store basket.
 * It contains the total price of the products in the basket and a list of products.
 */
@Data
@AllArgsConstructor
public class StoreBasketDTO {
  private double total; //total price of the products in the basket
  private List<Product> products; //list of products in the basket
}
