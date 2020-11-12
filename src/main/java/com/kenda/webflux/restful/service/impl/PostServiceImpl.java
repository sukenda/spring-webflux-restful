package com.kenda.webflux.restful.service.impl;

import com.kenda.webflux.restful.entity.Comment;
import com.kenda.webflux.restful.entity.Post;
import com.kenda.webflux.restful.exception.DataException;
import com.kenda.webflux.restful.model.PostRequest;
import com.kenda.webflux.restful.repository.PostRepository;
import com.kenda.webflux.restful.service.PostService;
import com.kenda.webflux.restful.service.ValidationService;
import com.kenda.webflux.restful.utils.GenericConverter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final ValidationService validationService;

    @Override
    public Mono<Post> save(PostRequest request) {
        validationService.validate(request);

        Post post = GenericConverter.mapper(request, Post.class);
        return postRepository.save(post);
    }

    @Override
    public Flux<Post> find() {
        return postRepository.findAll();
    }

    @Override
    public Mono<Post> findById(String id) {
        Mono<Post> mono = findByIdOrThrow(id);

        return mono.flatMap(Mono::just);
    }

    @Override
    public Mono<Post> update(String id, PostRequest request) {
        validationService.validate(request);

        Mono<Post> mono = findByIdOrThrow(id);

        return mono.flatMap(post -> {
            post.setBody(request.getBody());
            post.setTitle(request.getTitle());
            post.setComments(request.getComments());

            return postRepository.save(post);
        });
    }

    @Override
    public Mono<Post> comment(String id, Comment comment) {
        Mono<Post> mono = findByIdOrThrow(id);

        return mono.flatMap(post -> {
            post.getComments().add(comment);

            return postRepository.save(post);
        });
    }

    @Override
    public Mono<Post> delete(String id) {
        Mono<Post> mono = findByIdOrThrow(id);

        return mono.flatMap(post -> postRepository.delete(post).then(Mono.just(post)));
    }

    private Mono<Post> findByIdOrThrow(String id) {
        return postRepository.findById(id)
                .switchIfEmpty(Mono.error(new DataException("Data tidak ditemukan")))
                .flatMap(Mono::just);
    }
}
