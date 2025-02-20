package com.example.socialdrinks.feed.service;

import com.example.socialdrinks.feed.entity.*;
import com.example.socialdrinks.feed.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class RatingService {

    private final PostRepository postRepository;

    public RatingService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Double getAverageRating(Long cocktailId) {
        List<Post> posts = postRepository.findByCocktailId(cocktailId);
        if (posts.isEmpty()) {
            return null;
        }
        double sum = 0;
        int count = 0;
        for (Post post : posts) {
            if (post.getRating() != null) {
                sum += post.getRating();
                count++;
            }
        }
        return count > 0 ? sum / count : null;
    }

}
