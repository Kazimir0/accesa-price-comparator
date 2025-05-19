package com.pricecomp.accesa.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.pricecomp.accesa.model.DiscountedProduct;

@Service
public class BestDiscountService {
  /** 
   * Returns the top N discounted products, sorted by discount percentage (descending)
   * @param limit  how many products to return
   * @param allDiscounts all discounted products
   * @return  top discounted products
   * 
   */
  public List<DiscountedProduct> getTopDiscounts(int limit, List<DiscountedProduct> allDiscounts){
    return allDiscounts.stream()
        .sorted(Comparator.comparingDouble(DiscountedProduct::getDiscountPercentage).reversed())
        .limit(limit)
        .collect(Collectors.toList());
  }
}
