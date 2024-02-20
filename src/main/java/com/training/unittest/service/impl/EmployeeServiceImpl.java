package com.training.unittest.service.impl;

import com.training.unittest.exception.EmployeeExceptions;
import com.training.unittest.model.Employee;
import com.training.unittest.repository.EmployeeRepo;
import com.training.unittest.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepo employeeRepo;

    @Override
    public Employee saveEmployee(Employee employee) {
        Optional<Employee> byEmail = employeeRepo.findByEmail(employee.getEmail());
        if (byEmail.isPresent()) {
            throw new EmployeeExceptions("Employee already exists with given email: " + employee.getEmail());
        }
        return employeeRepo.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        Optional<Employee> byId = employeeRepo.findById(id);
        if (byId.isEmpty()) {
            throw new EmployeeExceptions("User: " + id + " does not exist");
        }
        return byId;
    }

    @Override
    public Employee updateEmployee(Employee updatedEmployee) {
        return employeeRepo.save(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepo.deleteById(id);
    }
}
