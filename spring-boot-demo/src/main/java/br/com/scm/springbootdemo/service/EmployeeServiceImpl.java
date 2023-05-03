package br.com.scm.springbootdemo.service;

import br.com.scm.springbootdemo.error.EmployeeNotFoundException;
import br.com.scm.springbootdemo.model.Employee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    List<Employee> employees = new ArrayList<>();

    @Override
    public Employee save(Employee employee) {

        if (employee.getEmployeeId() == null || employee.getEmailId().isEmpty()) {
            employee.setEmployeeId(UUID.randomUUID().toString());
        }

        employees.add(employee);

        return employee;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employees;
    }

    @Override
    public Employee getEmployeeById(String id) {
        return employees
                .stream()
                .filter(employee -> employee.getEmployeeId().equalsIgnoreCase(id))
                .findFirst()
                .orElseThrow(() -> new EmployeeNotFoundException("Employee id not found " + id));
    }
}
