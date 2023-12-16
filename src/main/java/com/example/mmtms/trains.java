package com.example.mmtms;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class trains {
    @GetMapping("/trains")
    public String getData() {return  "Please book train tickets from MMT at 15% discount" ; }
}