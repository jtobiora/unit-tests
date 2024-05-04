package com.swiftfingers.integrationtests;

import com.swiftfingers.integrationtests.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestH2Repository extends JpaRepository<Product,Integer> {
}
