package com.example.socialdrinks.feed.service;

import com.example.socialdrinks.feed.entity.*;
import com.example.socialdrinks.feed.repository.*;
import org.springframework.stereotype.*;
import reactor.core.publisher.*;
import reactor.core.scheduler.*;

import java.util.*;

@Service
public class TimelineService {

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final PostUpdateRepository postUpdateRepository;

    private final SubscriptionReactiveRepository subscriptionRepository;

    public TimelineService(UserRepository userRepository, PostRepository postRepository, PostUpdateRepository postUpdateRepository, SubscriptionReactiveRepository subscriptionRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.postUpdateRepository = postUpdateRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

/*
    private Flux<Post> streamPostsRequest(String server, List<String> creators) {
        return Flux.create(sink -> {
            String correlationId = UUID.randomUUID().toString();
            fluxMap.put(correlationId, sink);
            sink.onCancel(() -> {
                // Cleanup if the subscriber cancels
                // rabbitTemplate.convertAndSend("post.exchange", "post." + server + ".routing.key.cancel", creators);
                System.out.println("cancelled!");
            });

            MessageProperties properties = MessagePropertiesBuilder.newInstance()
                    .setReplyTo(replyRoutingKey)
                    .setCorrelationId(correlationId)
                    .build();

            MessageConverter messageConverter = rabbitTemplate.getMessageConverter();
            Message message = messageConverter.toMessage(creators, properties);

            rabbitTemplate.send(POST_EXCHANGE, postRoutingKey(server), message);
        });
    }
*/

/*
    @RabbitListener(queues = "#{replyQueue.name}")
    public void handleReplyQueue(@Header(CORRELATION_ID) String correlationId, @Payload Post post) {
        FluxSink<Post> sink = fluxMap.get(correlationId);
        if (sink != null) {
            sink.next(post);
        } else {
            System.out.println("unknown correlationId: " + correlationId + "post = " + post);
        }
    }
*/

/*
    @RabbitListener(queues = "#{postQueue.name}")
    public void handlePostQueue(@Header(REPLY_TO) String replyTo, @Header(CORRELATION_ID) String correlationId, List<String> creatorNames) {
        System.out.println("handlePostsRequest: replyTo = " + replyTo + ", correlationId = " + correlationId + ", creatorNames = " + creatorNames);
        List<User> creators = userRepository.findByUsernameIn(creatorNames);
        postUpdateRepository.streamByCreatorIn(creators)
                .subscribe(post -> {
                    System.out.println("post = " + post);

                    MessageProperties properties = MessagePropertiesBuilder.newInstance()
                            .setCorrelationId(correlationId)
                            .build();

                    MessageConverter messageConverter = rabbitTemplate.getMessageConverter();

                    Message message = messageConverter.toMessage(post, properties);
                    rabbitTemplate.send(REPLY_EXCHANGE, replyTo, message);
                });
    }
*/

    public Flux<Post> getTimelinePostUpdates(String subscriberName) {
        return Mono.fromCallable(() -> getUser(subscriberName)).subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(subscriber -> {
                    return subscriptionRepository.findBySubscriber(subscriber) // Reaktives Abrufen der Abonnements
                            .map(Subscription::getCreator) // Mapping zu den Creator-Usern
                            .startWith(subscriber) // Füge den Subscriber selbst am Anfang der Liste hinzu
                            .collectList() // Sammle alle als Liste
                            .flatMapMany(creators -> postUpdateRepository.streamByCreatorIn(creators)); // Posts abrufen
                });
    }

    /**
     * Erstellt einen neuen Post, der die Cocktail-ID, den Cocktail-Namen und das Rating enthält.
     */
    public void createPost(String creatorName, Long cocktailId, String cocktailName, Integer rating) {
        User creator = getUser(creatorName);
        Date now = new Date();

        // Erstelle einen neuen Post – hier solltest du die Post-Entity anpassen, sodass sie diese Felder enthält.
        Post post = new Post(creator, now, cocktailId, cocktailName, rating);
        postRepository.save(post);
    }

    private User getUser(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            user = new User(username);
            userRepository.save(user);
        }

        return user;
    }

    /**
     * Sucht blockierend nach Posts, deren Cocktailname den Suchbegriff enthält.
     */
    public List<Post> searchPosts(String query) {
        // Wir gehen davon aus, dass du in deinem PostRepository eine Methode hast,
        // die blockierend nach Cocktailnamen sucht.
        return postRepository.findByCocktailNameContaining(query);
    }

}
