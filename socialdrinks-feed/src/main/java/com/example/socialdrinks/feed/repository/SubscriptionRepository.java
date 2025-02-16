package com.example.socialdrinks.feed.repository;

import com.example.socialdrinks.feed.entity.*;
import org.springframework.data.mongodb.repository.*;

import java.util.*;

public interface SubscriptionRepository extends MongoRepository<Subscription, String> {

    long countByCreatorId(String creatorId);

    List<Subscription> findBySubscriber(User subscriber);

    List<Subscription> findByCreatorAndSubscriber(User creator, User subscriber);

}
