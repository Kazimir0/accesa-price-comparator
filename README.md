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

API endpoints:
1. GET request: http://localhost:8080/api/discounts/best?limit=5 |-> Returns the top 5 products with the highest current discount percentages (sorted descending). (default value of limit is 10).

Assumptions and Simplifications:
* The most recent discount file per store is selected by parsing the date from the filename
* Discounted products are not cross-matched with normal price files (i.e., they are standalone entries)
* CSV fields are expected to be separated by ',' and follow the format: product_id,product_name,brand,package_quantity,package_unit,product_category,from_date,to_date,percentage_of_discount
