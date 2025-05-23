package com.pricecomp.accesa.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import com.pricecomp.accesa.model.Product;

@Service
public class CsvLoaderService {

  private static final String PRODUCTS_FOLDER = "classpath:data/csv/*.csv";

  public List<Product> loadAllProducts() {
    List<Product> products = new ArrayList<>();
    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    try {
      // Load all CSV files from the specified folder
      Resource[] resources = resolver.getResources(PRODUCTS_FOLDER);
      // For each resource
      for (Resource resource : resources) {
        String fileName = resource.getFilename();
        // Skip files that are not in the expected format or contain "discounts"
        if (fileName == null || fileName.contains("discounts")) continue;
        
        // Extract the store name from the file name  
        String store = fileName.split("_")[0];
        // Read the products from the CSV file and add them to the list
        products.addAll(readProductFromCsv(resource, store));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return products;
  }

  private List<Product> readProductFromCsv(Resource resource, String store) {
    List<Product> products = new ArrayList<>();
    //Open the resource as a BufferedReader for reading
    try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
      String line;
      boolean firstLine = true;
      // Read each line from the CSV file
      while ((line = br.readLine()) != null) {
        // Skip the first line (header)
        if (firstLine) {
          firstLine = false;
          continue;
        }
        // Split the line by commas and check if it has the expected number of columns
        String[] values = line.split(",");
        if (values.length != 8) continue;

        // Create a new Product object and set its properties
        Product p = new Product();
        p.setProductId(values[0]);
        p.setProductName(values[1]);
        p.setProductCategory(values[2]);
        p.setBrand(values[3]);
        p.setPackageQuantity(Double.parseDouble(values[4]));
        p.setPackageUnit(values[5]);
        p.setPrice(Double.parseDouble(values[6]));
        p.setCurrency(values[7]);
        p.setStore(store);

        products.add(p);
      }
    } catch (Exception e) {
      System.out.println("Error reading resource: " + resource.getFilename());
      e.printStackTrace();
    }
    return products;
  }
}