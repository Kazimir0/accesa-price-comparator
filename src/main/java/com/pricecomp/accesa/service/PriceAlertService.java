package com.pricecomp.accesa.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;

import com.pricecomp.accesa.model.PriceAlertMatch;

@Service
public class PriceAlertService {
  private static final String PRICE_FILES_PATTERN = "classpath:data/csv/*_2025-*.csv";  // Pattern to match all non-discount product CSV files
  private static final DateTimeFormatter FILE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Format used to parse the date from the file name

  /**
   * Finds all product entries from CSV files that match a given product name
   * and have a price less than or equal to the user's target price.
   *
   * @param productName the product name to search (partial match, case-insensitive)
   * @param targetPrice the target price threshold
   * @return a list of matching products across all stores and dates
   */
  public List<PriceAlertMatch> findMatchesBelowTarget(String productName,double targetPrice) {
    List<PriceAlertMatch> matches = new ArrayList<>();
    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    try{
      Resource[] resources = resolver.getResources(PRICE_FILES_PATTERN); // Load all matching CSV files

      for(Resource resource : resources) {
        String fileName = resource.getFilename();

        if (fileName == null || fileName.contains("discount")) continue; // Skip files that are null or are discount-related

        // Extract store name and date from the file name
        String store = fileName.split("_")[0];
        String dateStr = fileName.split("_")[1].replace(".csv", "");
        LocalDate date = LocalDate.parse(dateStr, FILE_DATE_FORMAT);

        // Read and parse the CSV file
        try(BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
          String line;
          boolean firstLine = true;

          while((line = br.readLine()) != null) {
            if(firstLine){
              firstLine = false;
              continue;
            }

            String[] values = line.split(",");
            if (values.length < 8) continue;

            // Check if the product name contains the search term (case-insensitive)
            String csvProductName = values[1].toLowerCase().trim();
            if(!csvProductName.contains(productName.toLowerCase())) continue; 

            // Parse the price and compare with target
            double price = Double.parseDouble(values[6]);
            if(price > targetPrice) continue;

            PriceAlertMatch match = new PriceAlertMatch(
              values[1], // productName
              values[3], // Brand
              store,
              price,
              Double.parseDouble(values[4]),
              values[5], // packageUnit
              date.toString()
            );
            matches.add(match);
          }
        }
      }
      }catch (Exception e) {
            e.printStackTrace();
        }
    // Return all matching products across files
    return matches;
  } 
}