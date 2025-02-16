package com.example.socialdrinks.cocktails.repository;

import com.example.socialdrinks.model.entity.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface IngredientRepository extends CrudRepository<Ingredient, Long> {

    Collection<Ingredient> findAll();

    Collection<Ingredient> findByNameContains(String query);

}
