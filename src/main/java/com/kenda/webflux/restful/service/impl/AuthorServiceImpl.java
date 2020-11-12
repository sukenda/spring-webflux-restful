package com.kenda.webflux.restful.service.impl;

import com.kenda.webflux.restful.entity.Author;
import com.kenda.webflux.restful.exception.DataException;
import com.kenda.webflux.restful.model.AuthorRequest;
import com.kenda.webflux.restful.model.AuthorResponse;
import com.kenda.webflux.restful.model.RestResponse;
import com.kenda.webflux.restful.repository.AuthorRepository;
import com.kenda.webflux.restful.service.AuthorService;
import com.kenda.webflux.restful.service.ValidationService;
import com.kenda.webflux.restful.utils.GenericConverter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Created by Kenda on 12 Nov 2020
 * Email soekenda09@gmail.com
 **/
@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final ValidationService validationService;

    @Override
    public Mono<Author> save(AuthorRequest request) {
        validationService.validate(request);

        Author author = GenericConverter.mapper(request, Author.class);
        return authorRepository.save(author);
    }

    @Override
    public Mono<Author> update(String id, AuthorRequest request) {
        validationService.validate(request);

        Mono<Author> mono = findByIdOrThrow(id);

        return mono.flatMap(author -> {
            author.setFirstName(request.getFirstName());
            author.setLastName(request.getLastName());
            author.setCity(request.getCity());
            author.setGender(request.getGender());

            return authorRepository.save(author);
        });
    }

    @Override
    public Mono<Author> findById(String id) {
        Mono<Author> mono = findByIdOrThrow(id);

        return mono.flatMap(Mono::just);
    }

    @Override
    public Mono<RestResponse<List<AuthorResponse>>> find(String firstName, int page, int size) {
        RestResponse<List<AuthorResponse>> response = new RestResponse<>();

        return authorRepository.count()
                .map(rows -> {
                    response.setRows(Math.toIntExact(rows));
                    response.setStatus("OK");
                    response.setCode(200);

                    return rows;
                }).flatMapMany(elements -> authorRepository.findByFirstNameContainsIgnoreCase(firstName, PageRequest.of(page, size)))
                .collectList()
                .map(authors -> {
                    response.setData(GenericConverter.mapperList(authors, AuthorResponse.class));
                    return response;
                });
    }

    @Override
    public Mono<Author> delete(String id) {
        Mono<Author> mono = findByIdOrThrow(id);

        return mono.flatMap(author -> authorRepository.delete(author).then(Mono.just(author)));
    }

    private Mono<Author> findByIdOrThrow(String id) {
        return authorRepository.findById(id)
                .switchIfEmpty(Mono.error(new DataException("Data tidak ditemukan")))
                .flatMap(Mono::just);
    }
}
