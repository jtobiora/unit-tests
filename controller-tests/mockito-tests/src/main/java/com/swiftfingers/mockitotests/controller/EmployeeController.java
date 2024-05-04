package com.swiftfingers.mockitotests.controller;


import com.swiftfingers.mockitotests.entity.Employee;
import com.swiftfingers.mockitotests.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {

    private final EmployeeService empService;

    public EmployeeController(EmployeeService empService) {
        this.empService = empService;
    }

    /**
     * Add new employee
     *
     * @param employee
     * @return Added Employee
     */
    @PostMapping("/create")
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        empService.save(employee);
        log.debug("Added:: " + employee);
        return new ResponseEntity<Employee>(employee, HttpStatus.CREATED);
    }


    /**
     * Update existing employee
     *
     * @param employee
     * @return void
     */
    @PutMapping("/update")
    public ResponseEntity<Void> updateEmployee(@RequestBody Employee employee) {
        Employee existingEmp = empService.getById(employee.getId());
        if (existingEmp == null) {
            log.debug("Employee with id " + employee.getId() + " does not exists");
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        } else {
            empService.save(employee);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
    }


    /**
     * Get the Employee by ID
     *
     * @param id of Employee
     * @return Employee
     */
   @GetMapping("/findOne/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable("id") Long id) {
        Employee employee = empService.getById(id);
        if (employee == null) {
            log.debug("Employee with id " + id + " does not exists");
            return new ResponseEntity<Employee>(HttpStatus.NOT_FOUND);
        }
        log.debug("Found Employee:: " + employee);
        return new ResponseEntity<Employee>(employee, HttpStatus.OK);
    }


    /**
     * Get All Employees in DB
     *
     * @return List of all Employees
     */
    @GetMapping("/findAll")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = empService.getAll();
        if (employees.isEmpty()) {
            log.debug("Employees does not exists");
            return new ResponseEntity<List<Employee>>(HttpStatus.NO_CONTENT);
        }
        log.debug("Found " + employees.size() + " Employees");
        log.debug(Arrays.toString(employees.toArray()));
        return new ResponseEntity<List<Employee>>(employees, HttpStatus.OK);
    }


    /**
     * Delete Employee
     *
     * @param id of Employee to delete
     * @return void
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Long id) {
        Employee employee = empService.getById(id);
        if (employee == null) {
            log.debug("Employee with id " + id + " does not exists");
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        } else {
            empService.delete(id);
            log.debug("Employee with id " + id + " deleted");
            return new ResponseEntity<Void>(HttpStatus.GONE);
        }
    }
}
