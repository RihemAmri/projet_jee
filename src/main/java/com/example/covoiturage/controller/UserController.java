package com.example.covoiturage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
   @GetMapping({"","/"})
    public String home(){
       return "about";
   }
    @GetMapping({"/login",})
    public String login(){
        return "login";
    }


}
