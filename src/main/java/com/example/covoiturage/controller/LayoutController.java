package com.example.covoiturage.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Add this annotation to mark the class as a controller
public class LayoutController {

    @GetMapping("/about")
    public String aboutPage() {
        return "about";  // The name of your HTML page, no need to include the .html extension
    }
}
