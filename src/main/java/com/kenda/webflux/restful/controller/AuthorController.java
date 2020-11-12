package com.kenda.webflux.restful.controller;

import com.kenda.webflux.restful.model.*;
import com.kenda.webflux.restful.service.AuthorService;
import com.kenda.webflux.restful.utils.GenericConverter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Kenda on 12 Nov 2020
 * Email soekenda09@gmail.com
 **/
@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
@Tag(name = "Author", description = "API description for author")
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<RestResponse<AuthorResponse>> save(@Valid @RequestBody AuthorRequest request) {
        return authorService.save(request)
                .flatMap(author -> Mono.just(
                        new RestResponse<>(
                                "OK",
                                200,
                                GenericConverter.mapper(author, AuthorResponse.class),
                                1
                        )));
    }

    @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<RestResponse<List<AuthorResponse>>> find(@RequestParam(value = "firstName", required = false, defaultValue = "") String firstName,
                                                         @RequestParam("page") int page,
                                                         @RequestParam("size") int size) {

        return authorService.find(firstName, page, size);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<RestResponse<AuthorResponse>> findById(@PathVariable("id") String id) {
        return authorService.findById(id)
                .flatMap(posts -> Mono.just(
                        new RestResponse<>(
                                "OK",
                                200,
                                GenericConverter.mapper(posts, AuthorResponse.class),
                                1
                        )));
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<RestResponse<AuthorResponse>> update(@PathVariable("id") String id, @RequestBody AuthorRequest request) {
        return authorService.update(id, request)
                .flatMap(post -> Mono.just(
                        new RestResponse<>(
                                "OK",
                                200,
                                GenericConverter.mapper(post, AuthorResponse.class),
                                1
                        )));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<RestResponse<String>> delete(@PathVariable("id") String id) {
        return authorService.delete(id)
                .flatMap(post -> Mono.just(
                        new RestResponse<>(
                                "OK",
                                200,
                                id,
                                1
                        )));
    }

}
