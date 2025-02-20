package com.example.socialdrinks.feed.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitRatingConfig {

    public static final String RATING_EXCHANGE = "rating.exchange";
    public static final String RATING_QUEUE = "rating.request.queue";
    public static final String RATING_ROUTING_KEY = "rating.request";

    @Bean
    public DirectExchange ratingExchange() {
        return new DirectExchange(RATING_EXCHANGE);
    }

    @Bean
    public Queue ratingQueue() {
        return new Queue(RATING_QUEUE);
    }

    @Bean
    public Binding ratingBinding(Queue ratingQueue, DirectExchange ratingExchange) {
        return BindingBuilder.bind(ratingQueue).to(ratingExchange).with(RATING_ROUTING_KEY);
    }

}
