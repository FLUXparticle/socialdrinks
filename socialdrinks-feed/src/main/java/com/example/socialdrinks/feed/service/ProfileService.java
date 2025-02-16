package com.example.socialdrinks.feed.service;

import com.example.socialdrinks.feed.entity.*;
import com.example.socialdrinks.feed.repository.*;
import org.slf4j.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class ProfileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileService.class);

    private final UserRepository userRepository;

    private final SubscriptionRepository subscriptionRepository;

    public ProfileService(UserRepository userRepository, SubscriptionRepository subscriptionRepository) {
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public User getUser(String username, boolean create) {
        User user = userRepository.findByUsername(username);

        if (user == null && create) {
            user = new User(username);
            userRepository.save(user);
        }

        return user;
    }

    public long getSubscriberCount(String userId) {
        return subscriptionRepository.countByCreatorId(userId);
    }

    public String subscribe(String creatorServer, String creatorName, String subscriberName) {
        User creator = getUser(creatorName, false);
        if (creator == null) {
            return String.format("Creator '%s' existiert nicht.", creatorName);
        }

        User subscriber = getUser(subscriberName, true);

        List<Subscription> subscriptions = subscriptionRepository.findByCreatorAndSubscriber(creator, subscriber);
        if (!subscriptions.isEmpty()) {
            return String.format("Du hast '%s' schon abonniert.", creatorName);
        }

        Subscription subscription = new Subscription(creator, subscriber);

        subscriptionRepository.save(subscription);

        return "";
    }

}
