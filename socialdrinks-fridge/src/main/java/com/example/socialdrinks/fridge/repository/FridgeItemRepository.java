package com.example.socialdrinks.fridge.repository;

import com.example.socialdrinks.fridge.entity.FridgeItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FridgeItemRepository extends CrudRepository<FridgeItem, Long> {

    List<FridgeItem> findByUsername(String username);

    Optional<FridgeItem> findByUsernameAndIngredientId(String username, Long ingredientId);

    void deleteByUsernameAndIngredientId(String username, Long ingredientId);
}
