package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Employee;
import org.example.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Cacheable(value = "employee", key = "#id")
    public Employee getEmployee(Integer id){
        log.info("Fetching from MySql DB...");
        return employeeRepository.findById(id).orElse(null);
    }

    @CachePut(value = "employee", key = "#employee.id")
    public Employee updateEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @CacheEvict(value = "employee", key = "#id")
    public void deleteEmployee(Integer id) {
        employeeRepository.deleteById(id);
    }
}
