package com.swiftfingers.mockitotests.service;


import com.swiftfingers.mockitotests.entity.Employee;
import com.swiftfingers.mockitotests.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee save(Employee entity) {
        return employeeRepository.save(entity);
    }

    public Employee getById(Long id) {
        return employeeRepository.findById((Long) id).orElse(new Employee());
    }


    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }
}
