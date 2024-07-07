package com.example.jscom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.stream.Collectors;
@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/cart")
    public String addToCart(@RequestParam String productId, HttpSession session) {
        UserCpy user = (UserCpy) session.getAttribute("user");
        cartService.addToCart(productId, user.getId() );
        return "redirect:/products";
    }
    @GetMapping("/cart")
    public String viewCart(Model model,HttpSession session) {
        UserCpy user = (UserCpy) session.getAttribute("user");
        if(user!=null) 
            model.addAttribute("user",user);
        else
            return "redirect:/login";
        List<CartItem> cartItems = cartItemRepository.findByUserIdAndPurchased(user.getId(),false);
        List<CartItemWithProduct> cartItemsWithProduct = cartItems.stream().map(cartItem -> {
            Product product = productRepository.findById(cartItem.getProductId()).orElse(null);
            return new CartItemWithProduct(cartItem, product);
        }).collect(Collectors.toList());
        model.addAttribute("cartItems", cartItemsWithProduct);
        return "cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam String cartItemId, HttpSession session) {
        UserCpy user = (UserCpy) session.getAttribute("user");
        if(user==null) 
            return "redirect:/login";
        cartItemRepository.deleteById(cartItemId); 
        return "redirect:/cart"; 
    }
    @GetMapping("/orders")
    public String viewOrders(Model model,HttpSession session) {
        UserCpy user = (UserCpy) session.getAttribute("user");
        if(user!=null) 
            model.addAttribute("user",user);
        else
            return "redirect:/login";
        List<CartItem> cartItems = cartItemRepository.findByUserIdAndPurchased(user.getId(), true);
        List<CartItemWithProduct> cartItemsWithProductSuccess = cartItems.stream().map(cartItem -> {
            Product product = productRepository.findById(cartItem.getProductId()).orElse(null);
            return new CartItemWithProduct(cartItem, product);
        }).collect(Collectors.toList());
        model.addAttribute("orderSuccessItems", cartItemsWithProductSuccess);
        return "orders";
    }
}
