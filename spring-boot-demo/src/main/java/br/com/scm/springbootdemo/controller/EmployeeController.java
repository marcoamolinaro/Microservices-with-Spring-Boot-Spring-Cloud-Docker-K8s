package br.com.scm.springbootdemo.controller;

import br.com.scm.springbootdemo.model.Employee;
import br.com.scm.springbootdemo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public Employee save(@RequestBody Employee employee) {
        return  employeeService.save(employee);
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }
}
