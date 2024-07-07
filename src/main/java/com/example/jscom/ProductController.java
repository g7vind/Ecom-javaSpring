package com.example.jscom;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

import java.util.Base64;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private CartItemRepository cartItemRepository;

    @GetMapping("/products")
    public String listProducts(Model model, HttpSession session) {
        UserCpy user = (UserCpy) session.getAttribute("user");
        if(user!=null) 
            model.addAttribute("user",user);
        else
            return "redirect:/login";
        List<Product> productList = productRepository.findAll();
        Long cnt = 0L;
        if(cartItemRepository.count() != 0) {
            cnt = cartItemRepository.countByUserIdAndPurchased(user.getId(),false);
        }
        model.addAttribute("products", productList);
        model.addAttribute("cartSize", cnt);
        return "product-list";
    }

    @GetMapping("/addProduct")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "product-form";
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute("product") Product product,@RequestParam("image") MultipartFile imageFile) {
        if(!imageFile.isEmpty()) {
            try{
                byte[] imageBytes = imageFile.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                product.setImageData(base64Image);
                product.setImageType(imageFile.getContentType());
            }
            catch(Exception e){
                logger.error("Error while saving image",e);
                return "redirect:/addProduct";
            }
        }
        productRepository.save(product);
        return "redirect:/products";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

}
