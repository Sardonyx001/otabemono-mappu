package com.example.demo;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.opencsv.exceptions.CsvException;

import jakarta.servlet.http.HttpSession;


@Controller
public class LoginController {
    @GetMapping("/login")
    public String loginForm(){
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(
        @RequestParam ("username") String username,
        @RequestParam ("password") String password,
        HttpSession session,
        Model model
        ) throws IOException, CsvException {
        UserManager userManager = new UserManager();
        boolean isAuthenticated = userManager.Authenticate(username,password);
        boolean loginError = !isAuthenticated;
        model.addAttribute("loginError",loginError);
        session.setAttribute("username", username);
        session.setAttribute("password", password);
        session.setAttribute("isAuthenticated", isAuthenticated);
        return "redirect:/home";
    }

    @PostMapping("/logout")
    public String logoutSubmitAndRedirect(
        HttpSession session
        ){
        session.setAttribute("isAuthenticated",false);
        session.invalidate();
        return "redirect:/home";
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes,HttpSession session) {
        // Save the user data to the database or perform necessary actions
        // You can access the user data using the user object
        user.setUUID(UUID.randomUUID().toString());
        UserManager userManager = new UserManager();
        userManager.saveUser(user);
        redirectAttributes.addFlashAttribute("user", user);
        session.setAttribute("user",user);
        return "redirect:/signup-success";
    }

    @GetMapping("/signup-success")
    public String signupSuccess(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("user", user);
        return "signup-success";
    }
}