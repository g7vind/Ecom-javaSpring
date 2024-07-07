package com.example.jscom;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;


public interface CartItemRepository extends MongoRepository<CartItem, String> {
    List<CartItem> findByUserId(String userId);
    Long countByUserIdAndPurchased(String userId, Boolean purchased);
    List<CartItem> findByUserIdAndPurchased(String userId, Boolean purchased);

    @Query("{'userId': ?0, 'purchased': false}")
    @Update("{'$set': {'purchased': true}}")
    void markPurchased(String userId);
}
