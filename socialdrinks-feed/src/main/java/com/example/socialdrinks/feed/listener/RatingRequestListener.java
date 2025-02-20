package com.example.socialdrinks.feed.listener;

import com.example.socialdrinks.feed.config.*;
import com.example.socialdrinks.feed.service.*;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.*;

@Component
public class RatingRequestListener {

    private final RatingService ratingService;

    public RatingRequestListener(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @RabbitListener(queues = RabbitRatingConfig.RATING_QUEUE)
    public Double handleRatingRequest(Long cocktailId) {
        return ratingService.getAverageRating(cocktailId);
    }

}
