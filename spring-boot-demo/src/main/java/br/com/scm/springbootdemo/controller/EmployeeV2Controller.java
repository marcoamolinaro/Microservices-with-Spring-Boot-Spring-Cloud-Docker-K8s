package br.com.scm.springbootdemo.controller;

import br.com.scm.springbootdemo.model.Employee;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/employees")
public class EmployeeV2Controller {
    @PostMapping
    public Employee save(@RequestBody Employee employee) {
        return employee;
    }
}
