package com.kenda.webflux.restful.repository;

import com.kenda.webflux.restful.entity.Author;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {

    Flux<Author> findByFirstNameContainsIgnoreCase(String firstName, Pageable pageable);

}
