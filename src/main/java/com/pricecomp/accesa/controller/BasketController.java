package com.pricecomp.accesa.controller;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pricecomp.accesa.dto.StoreBasketDTO;
import com.pricecomp.accesa.model.Product;
import com.pricecomp.accesa.service.BasketOptimizerService;
import com.pricecomp.accesa.service.CsvLoaderService;
import java.util.*;

@RestController
@RequestMapping("/api/basket")
public class BasketController {

  private final CsvLoaderService csvLoaderService;
  private final BasketOptimizerService basketOptimizerService;

  // @Autowired
  public BasketController(CsvLoaderService csvLoaderService, BasketOptimizerService basketOptimizerService) {
    this.csvLoaderService = csvLoaderService;
    this.basketOptimizerService = basketOptimizerService;
  }

  @GetMapping("/optimize")
  public Map<String, Product> getOptimizedBasket(@RequestParam List<String> items) {
  // System.out.println("Received items for basket optimization: " + items);
  
  List<Product> allProducts = csvLoaderService.loadAllProducts();
  // System.out.println("Loaded " + allProducts.size() + " products from CSV files");
  
  return basketOptimizerService.optimizeBasket(items, allProducts);
  }

  @GetMapping("/optimize-by-store")
  public Map<String, StoreBasketDTO> getOptimizedBasketByStore(@RequestParam List<String> items) {

  List<Product> allProducts = csvLoaderService.loadAllProducts();

  // Return the optimized basket grouped by store
  return basketOptimizerService.optimizeAndGroupByStore(items, allProducts);
  }

}
