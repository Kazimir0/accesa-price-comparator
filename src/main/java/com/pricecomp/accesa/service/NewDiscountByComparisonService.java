package com.pricecomp.accesa.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.pricecomp.accesa.model.DiscountedProduct;

@Service
public class NewDiscountByComparisonService {

   public Map<String, List<DiscountedProduct>> getNewDiscountsGroupedByStore(
    List<DiscountedProduct> oldList, List<DiscountedProduct> newList) {

    Set<String> oldKeys = oldList.stream()
        .map(dp -> dp.getProductId() + "_" + dp.getFromDate())
        .collect(Collectors.toSet());

    return newList.stream()
        .filter(dp -> !oldKeys.contains(dp.getProductId() + "_" + dp.getFromDate()))
        .collect(Collectors.groupingBy(DiscountedProduct::getStore));
}
}
