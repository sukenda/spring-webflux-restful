package com.kenda.webflux.restful.service;

import com.kenda.webflux.restful.entity.Author;
import com.kenda.webflux.restful.exception.DataException;
import com.kenda.webflux.restful.model.AuthorRequest;
import com.kenda.webflux.restful.model.AuthorResponse;
import com.kenda.webflux.restful.model.RestResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Kenda on 12 Nov 2020
 * Email soekenda09@gmail.com
 **/
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = {"classpath:application.properties"})
class AuthorServiceImplTest {

    @Autowired
    private AuthorService authorService;

    private String authorId;

    private static ValidatorFactory validatorFactory;

    private static Validator validator;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
    @Order(1)
    @DisplayName("Create Author")
    void save() {
        AuthorRequest request = new AuthorRequest();
        request.setFirstName("Kenda");
        request.setLastName("Sukenda");
        request.setCity("Cirebon");
        request.setGender("Male");

        Mono<Author> mono = authorService.save(request);

        StepVerifier.create(mono)
                .consumeNextWith(author -> {
                    authorId = author.getId();
                    assertEquals("Kenda", author.getFirstName());
                })
                .expectComplete()
                .verify();
    }

    @Test
    @Order(2)
    @DisplayName("Create Author Not Valid")
    void saveNotValid() {
        AuthorRequest request = new AuthorRequest();
        request.setLastName("Sukenda");
        request.setCity("Cirebon");
        request.setGender("Male");

        Set<ConstraintViolation<AuthorRequest>> violations = validator.validate(request);
        violations.forEach(error -> assertEquals("must not be blank", error.getMessage()));
        assertFalse(violations.isEmpty());
    }

    @Test
    @Order(3)
    @DisplayName("Update Author")
    void update() {
        AuthorRequest request = new AuthorRequest();
        request.setFirstName("Kenda Update");
        request.setLastName("Sukenda Update");
        request.setCity("Cirebon Update");
        request.setGender("Male");

        Mono<Author> mono = authorService.update(authorId, request);
        StepVerifier.create(mono)
                .consumeNextWith(author -> assertEquals("Kenda Update", author.getFirstName()))
                .expectComplete()
                .verify();
    }

    @Test
    @Order(5)
    @DisplayName("Update Author Not Found")
    void updateNotFound() {
        AuthorRequest request = new AuthorRequest();
        request.setFirstName("Kenda Update");
        request.setLastName("Sukenda Update");
        request.setCity("Cirebon Update");
        request.setGender("Male");

        Mono<Author> mono = authorService.update("NotFound", request);
        StepVerifier.create(mono)
                .expectErrorMatches(throwable -> throwable instanceof DataException && throwable.getMessage().equals("Data tidak ditemukan")
                ).verify();
    }

    @Test
    @Order(6)
    @DisplayName("Find Author")
    void find() {
        Mono<RestResponse<List<AuthorResponse>>> mono = authorService.find("", 0, 10);

        StepVerifier.create(mono)
                .consumeNextWith(response -> assertNotNull(response.getData()))
                .expectComplete()
                .verify();
    }

    @Test
    @Order(7)
    @DisplayName("Find Author by id")
    void findById() {
        Mono<Author> mono = authorService.findById(authorId);

        StepVerifier.create(mono)
                .consumeNextWith(author -> assertNotNull(author.getFirstName()))
                .expectComplete()
                .verify();
    }

    @Test
    @Order(8)
    @DisplayName("Delete Author")
    void delete() {
        Mono<Author> mono = authorService.delete(authorId);

        StepVerifier.create(mono)
                .consumeNextWith(author -> assertEquals(authorId, author.getId()))
                .expectComplete()
                .verify();
    }

    @Test
    @Order(9)
    @DisplayName("Delete Author Not Found")
    void deleteNotFound() {
        Mono<Author> mono = authorService.delete(authorId);

        StepVerifier.create(mono)
                .expectErrorMatches(throwable -> throwable instanceof DataException && throwable.getMessage().equals("Data tidak ditemukan")
                ).verify();
    }
}