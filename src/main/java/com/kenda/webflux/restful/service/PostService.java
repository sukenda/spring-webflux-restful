package com.kenda.webflux.restful.service;

import com.kenda.webflux.restful.entity.Comment;
import com.kenda.webflux.restful.entity.Post;
import com.kenda.webflux.restful.model.PostRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PostService {

    Mono<Post> create(PostRequest request);

    Flux<Post> read();

    Mono<Post> update(String id, PostRequest request);

    Mono<Post> comment(String id, Comment comment);

    Mono<Post> delete(String id);

}
