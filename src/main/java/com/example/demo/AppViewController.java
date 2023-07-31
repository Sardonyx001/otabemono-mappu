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
    private final SharedStateService sharedStateService = new SharedStateService();

    private static boolean inputError = false;

    @GetMapping("/")
    String indexGet(){
        return "redirect:/home";
    }

    @GetMapping("/error")
    String error(){
        return "error";
    }

    @GetMapping("/home")
    public String home(
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
        sharedStateService.setFoodQueryList(apiController.getFoodQueryListAsStringList());

        model.addAttribute("inputValue", ""); 
        model.addAttribute("foodQueryList",sharedStateService.getFoodQueryList());
        model.addAttribute("inputError", inputError); 

        return "home";
    }

    @PostMapping("/home")
    public String resultPost(
        @RequestParam ("food") String food,
        HttpSession session,
        ModelMap model
    ) throws Exception{
        if(sharedStateService.getFoodQueryList().contains(food)){
            inputError = false;
            model.addAttribute("food", food);
            session.setAttribute("food", food);
            return "redirect:/apidata";
        }else{
            inputError = true;
            session.setAttribute("inputError", inputError);
            model.addAttribute("isAuthenticated", true);
            return "redirect:/home";
        }
    }
}
