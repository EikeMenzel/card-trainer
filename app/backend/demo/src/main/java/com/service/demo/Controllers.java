package com.service.demo;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
@RestController
public class Controllers {
    @GetMapping("/Hello")
    public String test() {
        return "Hello";
    }
}
