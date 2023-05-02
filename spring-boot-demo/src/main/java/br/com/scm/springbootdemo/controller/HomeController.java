package br.com.scm.springbootdemo.controller;

import br.com.scm.springbootdemo.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Hello World!";
    }

    @GetMapping("/user")
    public User user() {
        User user = new User("1", "Name 1", "email1@one.com");

        return user;
    }
}
