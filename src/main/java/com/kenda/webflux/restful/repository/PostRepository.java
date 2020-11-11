package com.kenda.webflux.restful.repository;

import com.kenda.webflux.restful.entity.Post;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends ReactiveMongoRepository<Post, String> {

}
