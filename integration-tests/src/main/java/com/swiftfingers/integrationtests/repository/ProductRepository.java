package com.swiftfingers.integrationtests.repository;


import com.swiftfingers.integrationtests.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    Product findByName(String name);
}