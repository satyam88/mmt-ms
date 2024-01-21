package com.example.mmtms;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class goa_trip {
    @GetMapping("/goa_trip")
    public String getData() {return  "Please book holidays from MMT at 55% discount" ; }
}