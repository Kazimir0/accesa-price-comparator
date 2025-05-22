package com.pricecomp.accesa.service;

import com.pricecomp.accesa.model.PricePoint;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PriceHistoryService {

    private static final String DISCOUNT_FILES_PATTERN = "classpath:data/csv/*_discounts_*.csv";
    private static final DateTimeFormatter FILE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<PricePoint> getPriceHistory(String productName, String category, Optional<String> optionalStore) {
        List<PricePoint> result = new ArrayList<>();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        try {
            Resource[] resources = resolver.getResources(DISCOUNT_FILES_PATTERN);

            for (Resource resource : resources) {
                String fileName = resource.getFilename();
                if (fileName == null) continue;

                // Extract store and date from filename
                String[] parts = fileName.split("_");
                if (parts.length < 3) continue;

                String store = parts[0];
                String dateStr = parts[2].replace(".csv", "");
                LocalDate fileDate = LocalDate.parse(dateStr, FILE_DATE_FORMAT);

                // Skip file if filtering by store and it doesn't match
                if (optionalStore.isPresent() && !optionalStore.get().equalsIgnoreCase(store)) continue;

                try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                    String line;
                    boolean firstLine = true;

                    while ((line = br.readLine()) != null) {
                        if (firstLine) {
                            firstLine = false;
                            continue;
                        }

                        String[] values = line.split(",");
                        if (values.length < 9) continue;

                        String csvProductName = values[1].toLowerCase().trim();
                        String csvCategory = values[5].toLowerCase().trim();

                        if (csvProductName.contains(productName.toLowerCase()) &&
                            csvCategory.equals(category.toLowerCase())) {

                            double discountPercentage = Double.parseDouble(values[8]);
                            result.add(new PricePoint(fileDate.toString(), discountPercentage));
                        }
                    }

                } catch (Exception e) {
                    System.out.println("Error reading file: " + fileName);
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Optional: sort by date ascending
        return result.stream()
                .sorted(Comparator.comparing(PricePoint::getDate))
                .collect(Collectors.toList());
    }
}
