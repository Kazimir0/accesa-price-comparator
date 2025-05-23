package com.pricecomp.accesa.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import com.pricecomp.accesa.model.DiscountedProduct;


/**
  * Service to load the latest discount files from the classpath.
  * Choose the latest file for each store based on the date in the filename.
  * Read the products from the latest files and return a list of DiscountedProduct objects.
 */
@Service
public class DiscountLoaderService {
  private static final String DISCOUNT_FOLDER = "classpath:data/csv/*_discounts_*.csv";
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public List<DiscountedProduct> loadLatestDiscounts() {
    List<DiscountedProduct> result = new ArrayList<>();
    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    try{
      Resource[] resources = resolver.getResources(DISCOUNT_FOLDER);
      Map<String, Resource> latestFiles = new HashMap<>();

      //Group files by store and keep the latest-dated one
      for(Resource resource : resources){
        String fileName = resource.getFilename();
        if(fileName == null) continue;

        String[] parts = fileName.split("_");
        if(parts.length < 3) continue;

        String store = parts[0];
        String datePart = parts[2].replace(".csv", ""); //2025-05-08
        LocalDate date = LocalDate.parse(datePart, FORMATTER);

        if(!latestFiles.containsKey(store)|| 
            date.isAfter(getDateFromFilename(latestFiles.get(store).getFilename()))){
          latestFiles.put(store, resource);
        }
      }
  

    //Load products from the latest discount files
    for(Map.Entry<String,Resource> entry : latestFiles.entrySet()){
      String store = entry.getKey();
      result.addAll(readDiscountFile(entry.getValue(), store));
    }
    }catch(Exception e){
      e.printStackTrace();
    }
    return result;
  }

  /**
   * Reads a discount file and returns a list of DiscountedProduct objects.
   * @param file The discount file to read.
   * @param store The store name from the filename.
   * @return A list of DiscountedProduct objects.
   */
  private List<DiscountedProduct> readDiscountFile(Resource file, String store) {
    List<DiscountedProduct> list = new ArrayList<>();

    try(BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
      String line;
      boolean firstLine = true;

      while((line = br.readLine()) != null){
        if(firstLine){
          firstLine = false; continue; //Skip header
        }
        String[] values = line.split(",");
        if(values.length < 9) continue; //Skip invalid lines

        DiscountedProduct dp = new DiscountedProduct();
        dp.setProductId(values[0]);
        dp.setProductName(values[1]);
        dp.setBrand(values[2]);
        dp.setPackageQuantity(Double.parseDouble(values[3]));
        dp.setPackageUnit(values[4]);
        dp.setProductCategory(values[5]);
        dp.setFromDate(values[6]);
        dp.setToDate(values[7]);
        dp.setDiscountPercentage(Double.parseDouble(values[8]));
        dp.setStore(store); // from filename

        list.add(dp);
      }
    } catch (Exception e) {
      System.out.println("Error reading discount file: " + file.getFilename());
      e.printStackTrace();
    }

    return list;
    
  }

  // Helper method to extract the date from the filename
  private LocalDate getDateFromFilename(String filename) {
    String[] parts = filename.split("_");
    String dateStr = parts[2].replace(".csv", "");
    
    return LocalDate.parse(dateStr, FORMATTER);
  }

  // return a list of DiscountedProduct objects from the file with the given date
  public List<DiscountedProduct> loadDiscountsForDate(String dateString) {
    
    // Prepare the result list to hold all loaded discounts
    List<DiscountedProduct> result = new ArrayList<>();

    // Spring utility for loading resources from the classpath
    // PathMatchingResourcePatternResolver is used to resolve resource patterns
    // "find me all files that match this pattern"
    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    try { // Load all files matching pattern "*_discounts_{date}.csv"
        Resource[] resources = resolver.getResources("classpath:data/csv/*_discounts_" + dateString + ".csv");

        for (Resource resource : resources) {
            String store = resource.getFilename().split("_")[0]; // Extract store name from filename
            result.addAll(readDiscountFile(resource, store)); // Read and parse the CSV file, then add all its products to the result list
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    // Return the complete list of discounts from all stores for the specified date
    return result;
}
}