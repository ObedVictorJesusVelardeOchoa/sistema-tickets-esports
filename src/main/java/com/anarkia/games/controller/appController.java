package com.anarkia.games.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class appController {
    @GetMapping("/")
    public String verPaginaPrincipal() {
        return "PaginaPrincipal";
    }

    @GetMapping("/Merch")
    public String verMerch() {
        return "Merch";
    }

}