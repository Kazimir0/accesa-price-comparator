package com.pricecomp.accesa.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.pricecomp.accesa.model.DiscountedProduct;

@Service
public class NewDiscountByComparisonService {

  public Map<String, List<DiscountedProduct>> getNewDiscountsGroupedByStore(
    List<DiscountedProduct> oldList, List<DiscountedProduct> newList) {

    // Build a set of keys (productId + fromDate) from the old list (previous snapshot)
    // These represent discounts that already existed
    Set<String> oldKeys = oldList.stream()
        .map(dp -> dp.getProductId() + "_" + dp.getFromDate())
        .collect(Collectors.toSet());

    /**  Stream through the new list (latest snapshot)
    Keep only those entries whose key is not in the old list (i.e., newly added discounts)
    Then group the results by store name
     */
    return newList.stream()
        .filter(dp -> !oldKeys.contains(dp.getProductId() + "_" + dp.getFromDate()))
        .collect(Collectors.groupingBy(DiscountedProduct::getStore));
}
}
