package com.example.jscom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
public class KeyController {

    @Value("${razorpay.key}")
    private String razorpayKey;

    @GetMapping("/key")
    public String getRazorPayKey(HttpSession session) {
        UserCpy user = (UserCpy) session.getAttribute("user");
        if(user == null){
            return "redirect:/login";
        }
        return razorpayKey;
    }
}