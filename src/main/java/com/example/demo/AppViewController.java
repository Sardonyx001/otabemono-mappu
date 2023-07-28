package com.example.demo;

import org.springframework.ui.ModelMap;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import jakarta.servlet.http.HttpSession;

@Controller
public class AppViewController{
    /**
     * @return Redirects root directly to /home
     */
    @GetMapping("/")
    String indexGet(){
        return "redirect:/home";
    }

    @GetMapping("/error")
    String error(){
        return "error";
    }

    @GetMapping("/home")
    String home(
        HttpSession session,
        ModelMap model
    ) throws Exception{
        // User user = (User)session.getAttribute("user");
        String username = (String)session.getAttribute("username");
        model.addAttribute("username",username);
        boolean isAuthenticated;
        try {
            isAuthenticated = (Boolean)session.getAttribute("isAuthenticated");
        } catch (Exception e) {
            isAuthenticated = false;
        }
        
        model.addAttribute("isAuthenticated",isAuthenticated);

        ApiController apiController = new ApiController();
        List<String> foodQueryList = apiController.getFoodQueryListAsStringList();
        System.out.println(foodQueryList);

        model.addAttribute("foodQueryList",foodQueryList);
        model.addAttribute("inputValue", ""); 

        return "home";
    }
    // TODO Should probably move query results to its own controller
    @GetMapping("/result")
    String resultGet(
        @RequestParam ("food") String food,
        HttpSession session,
        ModelMap model
    ){
        model.addAttribute("food", food);
        return "result";
    }

    @PostMapping("/result")
    String resultPost(
        @RequestParam ("food") String food,
        HttpSession session,
        ModelMap model
    ){
        session.setAttribute("food", food);
        model.addAttribute("food", food);
        return "redirect:/apidata";
    }
}
