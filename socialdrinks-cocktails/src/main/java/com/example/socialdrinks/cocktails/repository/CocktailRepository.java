package com.example.socialdrinks.cocktails.repository;

import com.example.socialdrinks.model.entity.*;
import org.springframework.data.repository.*;
import org.springframework.lang.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface CocktailRepository extends CrudRepository<Cocktail, Long> {

    @NonNull
    Collection<Cocktail> findAll();

}
