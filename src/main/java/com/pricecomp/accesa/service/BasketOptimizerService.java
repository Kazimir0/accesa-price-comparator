package com.pricecomp.accesa.service;

import com.pricecomp.accesa.dto.StoreBasketDTO;
import com.pricecomp.accesa.model.Product;
import org.springframework.stereotype.Service;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
/**
 * This service is responsible for optimizing the basket of products by selecting the cheapest option
 * for each product from a list of all available products.
 */
public class BasketOptimizerService {
  
  private static final Logger logger = LoggerFactory.getLogger(BasketOptimizerService.class);
  /**
   * Finds the cheapest product for each item name in the user's basket.
   * Returns a map of product name → cheapest product.
   */
  public Map<String, Product> optimizeBasket(List<String> productNames, List<Product> allProducts) {
    Map<String, Product> optimizedBasket = new HashMap<>();
    
    // Log the input product names and the total number of available products
    logger.info("Optimizing basket for products: {}", productNames);
    logger.info("Total available products: {}", allProducts.size());
    
    for(String name : productNames) {
      logger.info("Searching for product: '{}'", name);
      
      // For debugging, print some product names that might match
      allProducts.stream()
        .filter(p -> p.getProductName() != null)
        .limit(10)
        .forEach(p -> logger.info("Sample product in database: '{}'", p.getProductName()));
      
      // Find the cheapest product that matches the name (case insensitive)
      Optional<Product> cheapest = allProducts.stream()
        .filter(p -> p.getProductName() != null && 
                    p.getProductName().toLowerCase().contains(name.trim().toLowerCase()))
        .min(Comparator.comparing(Product::getPrice));
      
      // If a product is found, add it to the optimized basket
      if (cheapest.isPresent()) {
        logger.info("Found cheapest product: {} at price {}", cheapest.get().getProductName(), cheapest.get().getPrice());
        optimizedBasket.put(name, cheapest.get());
      } else {
        logger.warn("No product found matching: '{}'", name); // Log a warning if no product is found
      }
    }
    return optimizedBasket;  
  }

  /**
   * Optimizes the basket and groups products by store.
   * Returns a map of store name → StoreBasketDTO (includes total and list of products).
   */
  public Map<String, StoreBasketDTO> optimizeAndGroupByStore(List<String> productNames, List<Product> allProducts) {
    Map<String, List<Product>> grouped = new HashMap<>();

    for (String name : productNames) {
        Optional<Product> cheapest = allProducts.stream()
            .filter(p -> p.getProductName() != null &&
                        p.getProductName().toLowerCase().contains(name.trim().toLowerCase()))
            .min(Comparator.comparing(Product::getPrice));

        if (cheapest.isPresent()) {
            Product product = cheapest.get();
            String store = product.getStore();

            /**  
             * Check if the store already exists in the map;  if not, create a new list
             * Than add the product to the list
             * This replaces separate logic for check + create + add with a single line
            */
            grouped.computeIfAbsent(store, k -> new ArrayList<>()).add(product);
        }
    }

    Map<String, StoreBasketDTO> result = new HashMap<>();
    for (Map.Entry<String, List<Product>> entry : grouped.entrySet()) {
        String store = entry.getKey();
        List<Product> products = entry.getValue();
        double total = products.stream().mapToDouble(Product::getPrice).sum();

        result.put(store, new StoreBasketDTO(total, products));
    }
    return result;
}
}