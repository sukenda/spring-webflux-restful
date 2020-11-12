package com.kenda.webflux.restful.service;

import com.kenda.webflux.restful.entity.Author;
import com.kenda.webflux.restful.model.AuthorRequest;
import com.kenda.webflux.restful.model.AuthorResponse;
import com.kenda.webflux.restful.model.RestResponse;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Created by Kenda on 12 Nov 2020
 * Email soekenda09@gmail.com
 **/
public interface AuthorService {

    Mono<Author> save(AuthorRequest request);

    Mono<Author> update(String id, AuthorRequest request);

    Mono<Author> findById(String id);

    Mono<RestResponse<List<AuthorResponse>>> find(String firstName, int page, int size);

    Mono<Author> delete(String id);

}
