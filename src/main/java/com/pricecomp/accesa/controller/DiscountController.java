package com.pricecomp.accesa.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pricecomp.accesa.model.DiscountedProduct;
import com.pricecomp.accesa.model.PriceAlertMatch;
import com.pricecomp.accesa.model.PricePoint;
import com.pricecomp.accesa.model.RecommendedProduct;
import com.pricecomp.accesa.service.BestDiscountService;
import com.pricecomp.accesa.service.DiscountLoaderService;
import com.pricecomp.accesa.service.NewDiscountByComparisonService;
import com.pricecomp.accesa.service.PriceAlertService;
import com.pricecomp.accesa.service.PriceHistoryService;
import com.pricecomp.accesa.service.ProductRecommendationService;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

  private final DiscountLoaderService discountLoaderService;
  private final BestDiscountService bestDiscountService;
  private final NewDiscountByComparisonService newDiscountByComparisonService;
  private final PriceHistoryService priceHistoryService;
  private final ProductRecommendationService productRecommendationService;
  private final PriceAlertService priceAlertService;




  public DiscountController(
    DiscountLoaderService discountLoaderService, 
    BestDiscountService bestDiscountService,
    NewDiscountByComparisonService newDiscountByComparisonService,
    PriceHistoryService priceHistoryService, 
    ProductRecommendationService productRecommendationService,
    PriceAlertService priceAlertService) {
    
    this.discountLoaderService = discountLoaderService;
    this.bestDiscountService = bestDiscountService;
    this.newDiscountByComparisonService = new NewDiscountByComparisonService();
    this.priceHistoryService = priceHistoryService;
    this.productRecommendationService = productRecommendationService;
    this.priceAlertService = priceAlertService;
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
    // List<DiscountedProduct> oldDiscounts = discountLoaderService.loadDiscountsForDate("2025-05-01");
    List<DiscountedProduct> oldDiscounts = discountLoaderService.loadDiscountsForDate("2025-05-08");
    List<DiscountedProduct> newDiscounts = discountLoaderService.loadDiscountsForDate("2025-05-15");

    return newDiscountByComparisonService.getNewDiscountsGroupedByStore(oldDiscounts, newDiscounts);
  }

  // Returns the price history of a product across different stores
  @GetMapping("/price-history")
  public List<PricePoint> getPriceHistory(@RequestParam String productName,
                                        @RequestParam String category,
                                        @RequestParam(required = false) String store) {
    return priceHistoryService.getPriceHistory(productName, category, Optional.ofNullable(store));
  }
  
  // Returns recommended products based on value per unit (e.g., price per kg or liter) 
  @GetMapping("/recommendations")
  public List<RecommendedProduct> getRecommendedProducts(@RequestParam String productName,
                                                      @RequestParam(required = false) String category) {
    return productRecommendationService.getRecommendedProducts(productName, Optional.ofNullable(category));
  }

  // Returns products that match the user's target price alert
  @GetMapping("/price-alert")
  public List<PriceAlertMatch> getPriceAlerts(@RequestParam String productName,
                                            @RequestParam double target) {
    return priceAlertService.findMatchesBelowTarget(productName, target);
  }

}
