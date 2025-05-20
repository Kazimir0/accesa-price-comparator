package com.pricecomp.accesa.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pricecomp.accesa.model.DiscountedProduct;
import com.pricecomp.accesa.model.PricePoint;
import com.pricecomp.accesa.service.BestDiscountService;
import com.pricecomp.accesa.service.DiscountLoaderService;
import com.pricecomp.accesa.service.NewDiscountByComparisonService;
import com.pricecomp.accesa.service.PriceHistoryService;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

  private final DiscountLoaderService discountLoaderService;
  private final BestDiscountService bestDiscountService;
  private final NewDiscountByComparisonService newDiscountByComparisonService;
  private final PriceHistoryService priceHistoryService;


  public DiscountController(DiscountLoaderService discountLoaderService, BestDiscountService bestDiscountService,NewDiscountByComparisonService newDiscountByComparisonService,PriceHistoryService priceHistoryService) {
    this.discountLoaderService = discountLoaderService;
    this.bestDiscountService = bestDiscountService;
    this.newDiscountByComparisonService = new NewDiscountByComparisonService();
    this.priceHistoryService = priceHistoryService;
    
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

  @GetMapping("/new/by-comparison")
  public Map<String, List<DiscountedProduct>> getNewDiscountsGroupedByStore() {
    List<DiscountedProduct> oldDiscounts = discountLoaderService.loadDiscountsForDate("2025-05-01");
    List<DiscountedProduct> newDiscounts = discountLoaderService.loadDiscountsForDate("2025-05-08");

    return newDiscountByComparisonService.getNewDiscountsGroupedByStore(oldDiscounts, newDiscounts);
  }

  @GetMapping("/price-history")
  public List<PricePoint> getPriceHistory(@RequestParam String productName,
                                        @RequestParam String category,
                                        @RequestParam(required = false) String store) {
    return priceHistoryService.getPriceHistory(productName, category, Optional.ofNullable(store));
  }

}
