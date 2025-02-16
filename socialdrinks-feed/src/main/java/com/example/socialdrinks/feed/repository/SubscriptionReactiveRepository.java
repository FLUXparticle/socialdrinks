package com.example.socialdrinks.feed.repository;

import com.example.socialdrinks.feed.entity.*;
import org.springframework.data.mongodb.repository.*;
import reactor.core.publisher.*;

public interface SubscriptionReactiveRepository extends ReactiveMongoRepository<Subscription, String> {

    Flux<Subscription> findBySubscriber(User subscriber);

}
