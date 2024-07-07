package com.example.jscom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import jakarta.servlet.http.HttpSession;

@RestController
public class PaymentController {
    
    @Autowired
    private CartService cartService;

    @PostMapping("/cart/checkout")
    public ResponseEntity<Map<String, String>> handlePaymentSuccess(@RequestBody Map<String, String> paymentData, HttpSession session) {
        UserCpy user = (UserCpy) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "User not logged in."));
        }
        String userId = paymentData.get("user_Id");
        cartService.markPurchased(userId);
        return ResponseEntity.ok(Map.of("message", "Success"));
    }

}
