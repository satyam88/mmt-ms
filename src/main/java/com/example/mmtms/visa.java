package com.example.mmtms;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class visa {
    @GetMapping("/visa")
    public String getData() {return  "Please book visa for dubai at Rs 6750 only" ; }
}