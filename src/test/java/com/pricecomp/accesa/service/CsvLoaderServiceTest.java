package com.pricecomp.accesa.service;

import com.pricecomp.accesa.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CsvLoaderServiceTest {

    @Autowired
    private CsvLoaderService csvLoaderService;

    @Test
    public void testLoadAllProducts() {
        List<Product> products = csvLoaderService.loadAllProducts();

        // Afișăm în consolă primele produse
        products.stream().limit(5).forEach(p ->
                System.out.println(p.getProductName() + " - " + p.getStore() + " - " + p.getPrice()));

        // Verificăm că lista NU este goală
        assertFalse(products.isEmpty(), "Lista de produse nu trebuie să fie goală!");
    }
}
