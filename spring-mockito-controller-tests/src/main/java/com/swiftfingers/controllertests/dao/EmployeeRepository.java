package com.swiftfingers.controllertests.dao;


import com.swiftfingers.controllertests.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

}
