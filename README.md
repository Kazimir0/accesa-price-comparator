# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.3.11/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.3.11/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.3.11/reference/web/servlet.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

### Task 1:  Daily Shopping Basket Monitoring.
Description: Helps users split their shopping basket to optimize cost savings across different stores.
Implemented features:
1. Loads product data from multiple CSV files
2. Matches requested product names (case-insensitive)
3. Finds the cheapest product available across stores
4. Groups products by store
5. Calculates and returns total price per store

API endpoints:
1. GET request: http://localhost:8080/api/basket/optimize?items=banane,lapte,cașcaval              |-> Returns the cheapest product per item.
2. GET request: http://localhost:8080/api/basket/optimize-by-store?items=banane,lapte,cașcaval     |-> Groups optimized products by store and returns the total cost for each store.

Assumptions and Simplifications:
* Product names are compared using contains(...) logic, case-insensitive
* Product quantities and units are not used in matching logic
* Any CSV file that contains "discount" in its name is excluded from loading
* No frontend is included — application is tested using Postman
* Lombok (@Data) is used in the Product model to reduce boilerplate (getters/setters)

### Task 2:  Best Discounts.
Description: Lists products with the highest current percentage discounts across all tracked stores.
Implemented features:
1. Loads only the most recent discount CSV file per store (desc)
2. Parses discount data including product name, brand, and discount percentage
3. Sorts all discounted products by percentage in descending order
4. Returns the top N best discounted products (limit configurable)

API endpoint:
1. GET request: http://localhost:8080/api/discounts/best?limit=5 |-> Returns the top 5 products with the highest current discount percentages (sorted descending). (default value of limit is 10).

Assumptions and Simplifications:
* The most recent discount file per store is selected by parsing the date from the filename
* Discounted products are not cross-matched with normal price files (i.e., they are standalone entries)
* CSV fields are expected to be separated by ',' and follow the format: product_id,product_name,brand,package_quantity,package_unit,product_category,from_date,to_date,percentage_of_discount

### Task 3: New Discounts.
Description: Lists discounts that have been newly added by comparing the current discount snapshot against a previous one. ("2025-05-01" VS "2025-05-08")
Implemented features:
1. Loads discount data from two different dates (older and newer CSV files)
2. Identifies new discounts by comparing entries between the two datasets
3. A discount is considered “new” if it appears in the newer file but not in the older one ("2025-05-01" VS "2025-05-08")
4. Returns the new discounts grouped by store (e.g., Lidl, Profi, Kaufland) for better readability

API endpoint:
1. GET  request: http://localhost:8080/api/discounts/new/by-comparison |-> Returns all newly added discounts grouped by store (based on comparing discount CSV files from 2025-05-01 and 2025-05-08).

Assumptions and Simplifications:
* Discounts are considered "new" if they are present in the newer discount files "2025-05-08" and do not exist in the older discount files "2025-05-01", based on a key composed of productId + fromDate
* The response is grouped by store (based on filename prefix: lidl, profi, kaufland)
* File date matching is done via the *_discounts_YYYY-MM-DD.csv pattern using Spring’s resource loader (lidl_discounts_2025-05-01.csv | profi_discounts_2025-05-01.csv | kaufland_discounts_2025-05-01.csv)
* All responses are tested via Postman (GET: http://localhost:8080/api/discounts/new/by-comparison ), no frontend included
* A discount is considered "new" not just because of its fromDate, but because it did not exist in the previous file. The logic compares actual presence across the two snapshots.

### Task 4: Dynamic Price History Graphs
Description: Provides data points that allow a frontend to calculate and display discount trends over time for individual products.
Implemented features:
1. Loads all available discount CSV files (for every market)
2. Extracts and returns discount values over time for a specific product
3. Filters results by required parameters: 'productName' and 'category'
4. Supports optional filtering by 'store' for more granular control
5. Returns data as a chronological list of points with date and discount percentage

API endpoints:
1. GET request: http://localhost:8080/api/discounts/price-history?productName=iaurt grecesc&category=lactate |-> Returns a list of {date,discountPercentage} points for the specified product and category.

2. GET request: http://localhost:8080/api/discounts/price-history?productName=iaurt grecesc&category=lactate&store=Lidl |-> optional filter by store name (filter results by a specific store:Lidl,Kaufland,Profi)

Assumptions and Simplifications:
* The response is sorted by date in ascending order to help frontend graph rendering
* Product filtering is based on productName (case-insensitive, contains logic) and exact match on category
* If multiple discounts exist on the same day for the same product (in different stores) all are returned
* No grouping is applied in the response (each entry is a flat data point); grouping logic can be handled in the frontend if needed
* Application is tested via Postman — no frontend is implemented

### Task 5: Product Substitutes & Recommendations
Description: Helps users identify the best product alternatives by highlighting value per unit (price per kg or liter), allowing fair comparison across different pack sizes.
Implemented features:
1. Loads product data from all CSV files that represent regular prices (excluding discount files)
2. Calculates ' valuePerUnit = price / packageQuantity ' for each product
3. Filters products by ' productName ' (case-insensitive, contains logic)
4. Optional filtering by productCategory (exact match)
5. Sorts all matched products by valuePerUnit in ascending order (best value first)
6. Returns a list of recommended product options across all stores

API endpoints:
1. GET request: http://localhost:8080/api/discounts/recommendations?productName=iaurt |-> Returns a list of recommended product alternatives based on the lowest unit value
2. GET request: http://localhost:8080/api/discounts/recommendations?productName=iaurt&category=lactate |-> Optional filter (category)

Assumptions and Simplifications:
* Only CSV files without "discount" in the filename are used to retrieve full-price data
* Product filtering uses case-insensitive substring matching on ' productName '
* Products are compared based on their normalized unit price (price per kg or liter)
* The valuePerUnit is calculated even if package sizes differ, allowing better recommendation logic
* Each result includes product details, price, unit size, and store source
* Results are sorted by unit value, not total price, to reflect true cost efficiency
* Application is tested via Postman — no frontend is implemented

### Task 6: Custom Price Alert
Description: Allows users to set a target price for a product. The system identifies all current products that are at or below the specified price threshold.
Implemented features:
1. Loads all available product price CSV files (excluding discount files)
2. Searches for products whose name matches the input (case-insensitive, contains(...))
3. Filters the results by 'price <= target_price'
4. Returns a list of product matches that meet or fall below the user’s target price
5. Each result includes the product name, store, actual price, pack size, and the date the price was recorded

API endpoint:
1. GET request: http://localhost:8080/api/discounts/price-alert?productName=morcovi&target=2.5 |-> Returns all product entries (from all stores and dates (no included 'discounts' .csv files)) matching the name "morcovi" and priced at 2.5 RON or lower (just an example)

Assumptions and Simplifications:
* Only non-discount files are used
* Product name filtering uses case-insensitive substring logic (contains(...))
* The date of each price match is extracted from the filename (ex: "kaufland_2025-05-01.csv")
* Price comparisons are made using exact numeric value
* Application is tested via Postman — no frontend is implemented
