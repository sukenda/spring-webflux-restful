package com.kenda.webflux.restful.controller;

import com.kenda.webflux.restful.entity.Comment;
import com.kenda.webflux.restful.model.PostRequest;
import com.kenda.webflux.restful.model.PostResponse;
import com.kenda.webflux.restful.model.RestResponse;
import com.kenda.webflux.restful.service.PostService;
import com.kenda.webflux.restful.utils.GenericConverter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Tag(name = "Post", description = "API description for post")
public class PostController {

    private final PostService postService;

    @PostMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<RestResponse<PostResponse>> save(@RequestBody PostRequest request) {
        return postService.save(request)
                .flatMap(post -> Mono.just(
                        new RestResponse<>(
                                "OK",
                                200,
                                GenericConverter.mapper(post, PostResponse.class),
                                1
                        )));
    }

    @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<RestResponse<List<PostResponse>>> find() {
        return postService.find()
                .collectList()
                .flatMap(posts -> Mono.just(
                        new RestResponse<>(
                                "OK",
                                200,
                                GenericConverter.mapperList(posts, PostResponse.class),
                                posts.size()
                        )));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<RestResponse<PostResponse>> findById(@PathVariable("id") String id) {
        return postService.findById(id)
                .flatMap(posts -> Mono.just(
                        new RestResponse<>(
                                "OK",
                                200,
                                GenericConverter.mapper(posts, PostResponse.class),
                                1
                        )));
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<RestResponse<PostResponse>> update(@PathVariable("id") String id, @RequestBody PostRequest request) {
        return postService.update(id, request)
                .flatMap(post -> Mono.just(
                        new RestResponse<>(
                                "OK",
                                200,
                                GenericConverter.mapper(post, PostResponse.class),
                                1
                        )));
    }

    @PutMapping(value = "/{id}/comment", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<RestResponse<PostResponse>> comment(@PathVariable("id") String id, @RequestBody Comment request) {
        return postService.comment(id, request)
                .flatMap(post -> Mono.just(
                        new RestResponse<>(
                                "OK",
                                200,
                                GenericConverter.mapper(post, PostResponse.class),
                                1
                        )));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<RestResponse<String>> delete(@PathVariable("id") String id) {
        return postService.delete(id)
                .flatMap(post -> Mono.just(
                        new RestResponse<>(
                                "OK",
                                200,
                                id,
                                1
                        )));
    }

}
