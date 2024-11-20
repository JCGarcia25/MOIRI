package com.MSGFCentralSys.MSGFCentralSys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/home","/"})
    public String mainView(Model model) {
        model.addAttribute("titulo","Bienvenido al sistema OIRI");
        return "views/init";
    }
}


