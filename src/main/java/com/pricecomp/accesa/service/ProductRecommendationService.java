package com.pricecomp.accesa.service;

import com.pricecomp.accesa.model.RecommendedProduct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductRecommendationService {

    private static final String PRODUCT_FILES_PATTERN = "classpath:data/csv/*_2025-*.csv";

    public List<RecommendedProduct> getRecommendedProducts(String productName, Optional<String> optionalCategory) {
        List<RecommendedProduct> result = new ArrayList<>();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        try {
            Resource[] resources = resolver.getResources(PRODUCT_FILES_PATTERN);

            for (Resource resource : resources) {
                String fileName = resource.getFilename();
                if (fileName == null || fileName.contains("discount")) continue; // Ignore discount files

                String store = fileName.split("_")[0]; // Extract store name from filename

                try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                    String line;
                    boolean firstLine = true;

                    // Skip the first line (header)
                    while ((line = br.readLine()) != null) {
                        if (firstLine) {
                            firstLine = false; 
                            continue;
                        }
                        // Split the line by comma
                        String[] values = line.split(",");
                        if (values.length < 8) continue; // Ensure there are enough columns

                        String csvProductName = values[1].toLowerCase().trim(); // Product name in CSV
                        String category = values[2].toLowerCase().trim(); // Category in CSV

                        // Apply filters
                        if (!csvProductName.contains(productName.toLowerCase())) continue;  // Filter by product name)
                        if (optionalCategory.isPresent() && !category.equals(optionalCategory.get().toLowerCase())) continue;

                        // Parse the values
                        double packageQuantity = Double.parseDouble(values[4]); // Package quantity from csv 
                        String packageUnit = values[5]; // Package unit from csv
                        double price = Double.parseDouble(values[6]); // Price from csv

                        double valuePerUnit = price / packageQuantity; // Calculate value per unit

                        // Create a RecommendedProduct object
                        RecommendedProduct rp = new RecommendedProduct(
                                values[1],              // productName from csv
                                values[3],              // brand from csv
                                values[2],              // category from csv
                                packageQuantity,
                                packageUnit,
                                price,
                                store,
                                valuePerUnit
                        );

                        result.add(rp);
                    }

                } catch (Exception e) {
                    System.out.println("Error reading file: " + fileName);
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Sort by best value per unit (ascending)
        return result.stream()
                .sorted(Comparator.comparingDouble(RecommendedProduct::getValuePerUnit))
                .collect(Collectors.toList());
    }
}
