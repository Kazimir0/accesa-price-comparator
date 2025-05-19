package com.pricecomp.accesa.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pricecomp.accesa.model.DiscountedProduct;
import com.pricecomp.accesa.service.BestDiscountService;
import com.pricecomp.accesa.service.DiscountLoaderService;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

  private final DiscountLoaderService discountLoaderService;
  private final BestDiscountService bestDiscountService;

  public DiscountController(DiscountLoaderService discountLoaderService, BestDiscountService bestDiscountService) {
    this.discountLoaderService = discountLoaderService;
    this.bestDiscountService = bestDiscountService;
  }

  /**
   * Returns the top discounted products across all stores.
   * Example: /api/discounts/best?limit=5
  */
  @GetMapping("/best")
  public List<DiscountedProduct> getTopDiscounts(@RequestParam(defaultValue = "10") int limit) {
    List<DiscountedProduct> allDiscounts = discountLoaderService.loadLatestDiscounts();
    
    return bestDiscountService.getTopDiscounts(limit, allDiscounts);
  }
}
