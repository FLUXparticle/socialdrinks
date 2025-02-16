package com.example.socialdrinks.feed.repository;

import com.example.socialdrinks.feed.entity.*;
import org.springframework.data.mongodb.repository.*;

public interface PostReactiveRepository extends ReactiveMongoRepository<Post, String> {

}
