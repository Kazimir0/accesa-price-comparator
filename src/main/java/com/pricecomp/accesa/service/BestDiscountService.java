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
    // I used stream to convert the list to a stream(processing flow), to use the sorted,limit and collect methods
    // The normal 'List' does not have these methods
    return allDiscounts.stream()
        .sorted(Comparator.comparingDouble(DiscountedProduct::getDiscountPercentage).reversed()) // Sort by discount percentage in descending order
        .limit(limit)
        .collect(Collectors.toList()); // Convert the stream back to a list
  }
}
