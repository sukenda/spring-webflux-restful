package com.kenda.webflux.restful.controller;

import com.kenda.webflux.restful.entity.Comment;
import com.kenda.webflux.restful.model.PostRequest;
import com.kenda.webflux.restful.model.PostResponse;
import com.kenda.webflux.restful.model.RestResponse;
import com.kenda.webflux.restful.service.PostService;
import com.kenda.webflux.restful.utils.GenericConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Mono<RestResponse<PostResponse>> create(@RequestBody PostRequest request) {
        return postService.create(request)
                .flatMap(post -> Mono.just(
                        new RestResponse<>(
                                "OK",
                                200,
                                GenericConverter.mapper(post, PostResponse.class)
                        )));
    }

    @GetMapping
    public Mono<RestResponse<List<PostResponse>>> read() {
        return postService.read()
                .collectList()
                .flatMap(posts -> Mono.just(
                        new RestResponse<>(
                                "OK",
                                200,
                                GenericConverter.mapperList(posts, PostResponse.class)
                        )));
    }

    @PutMapping("/{id}")
    public Mono<RestResponse<PostResponse>> update(@PathVariable("id") String id, @RequestBody PostRequest request) {
        return postService.update(id, request)
                .flatMap(post -> Mono.just(
                        new RestResponse<>(
                                "OK",
                                200,
                                GenericConverter.mapper(post, PostResponse.class)
                        )));
    }

    @PutMapping("/{id}/comment")
    public Mono<RestResponse<PostResponse>> comment(@PathVariable("id") String id, @RequestBody Comment request) {
        return postService.comment(id, request)
                .flatMap(post -> Mono.just(
                        new RestResponse<>(
                                "OK",
                                200,
                                GenericConverter.mapper(post, PostResponse.class)
                        )));
    }

    @DeleteMapping("/{id}")
    public Mono<RestResponse<String>> delete(@PathVariable("id") String id) {
        return postService.delete(id)
                .flatMap(post -> Mono.just(
                        new RestResponse<>(
                                "OK",
                                200,
                                id
                        )));
    }

}
