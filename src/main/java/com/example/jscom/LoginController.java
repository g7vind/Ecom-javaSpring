package com.example.jscom;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ModelAttribute;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("user", new User());
        return "login-form";
    }
    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup-form";
    }
    @PostMapping("/login")
    public  String loginUser(@ModelAttribute("user") User user,HttpSession session) {
        User foundUser = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
        UserCpy userCpy = new UserCpy();
        if (foundUser != null) {
        userCpy.setEmail(foundUser.getEmail());
        userCpy.setName(foundUser.getName());
        userCpy.setId(foundUser.getId());
        userCpy.setPhone(foundUser.getPhone());
                session.setAttribute("user",userCpy);
                return "redirect:/products"; 
        } else {
                return "redirect:/login";
        }
        
    }
    @PostMapping("/signup")
    public String signupUser(@ModelAttribute("user") User user) {
        userRepository.save(user);
        return "redirect:/login";
    }

}
