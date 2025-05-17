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

### Task 2: