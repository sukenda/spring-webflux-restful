package com.kenda.webflux.restful.service;

import com.kenda.webflux.restful.entity.Comment;
import com.kenda.webflux.restful.entity.Post;
import com.kenda.webflux.restful.exception.DataException;
import com.kenda.webflux.restful.model.PostRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.validation.*;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PostServiceImplTest {

    @Autowired
    private PostService postService;

    private String postId;

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
    @DisplayName("Create Post")
    void create() {
        PostRequest request = new PostRequest();
        request.setTitle("Judul");
        request.setBody("Body");
        request.setComments(Arrays.asList(new Comment("Kenda 1", "Body ke 1"), new Comment("Kenda 2", "Body ke 2")));

        Mono<Post> mono = postService.save(request);

        StepVerifier.create(mono)
                .consumeNextWith(post -> {
                    postId = post.getId();
                    assertEquals("Body", post.getBody());
                })
                .expectComplete()
                .verify();
    }

    @Test
    @Order(2)
    @DisplayName("Create Post Not Valid")
    void createNotValid() {
        PostRequest request = new PostRequest();
        request.setTitle("Judul");
        request.setComments(Arrays.asList(new Comment("Kenda 1", "Body ke 1"), new Comment("Kenda 2", "Body ke 2")));

        Set<ConstraintViolation<PostRequest>> violations = validator.validate(request);
        violations.forEach(error -> assertEquals("must not be blank", error.getMessage()));
        assertFalse(violations.isEmpty());
    }

    @Test
    @Order(3)
    @DisplayName("Update Post")
    void update() {
        PostRequest request = new PostRequest();
        request.setTitle("Judul Update");
        request.setBody("Body Update");
        request.setComments(Arrays.asList(new Comment("Kenda 1 Update", "Body ke 1 Update"), new Comment("Kenda 2 Update", "Body ke 2 Update")));

        Mono<Post> mono = postService.update(postId, request);
        StepVerifier.create(mono)
                .consumeNextWith(post -> {
                    assertEquals("Judul Update", post.getTitle());
                    assertEquals("Body Update", post.getBody());
                })
                .expectComplete()
                .verify();
    }

    @Test
    @Order(4)
    @DisplayName("Add Comment Post")
    void comment() {
        Comment request = new Comment("Add Comment", "Body Add Comment");

        Mono<Post> mono = postService.comment(postId, request);
        StepVerifier.create(mono)
                .consumeNextWith(post -> {
                    Comment comment = post.getComments().get(post.getComments().size() - 1);
                    assertEquals("Body Add Comment", comment.getBody());
                })
                .expectComplete()
                .verify();
    }

    @Test
    @Order(5)
    @DisplayName("Update Post Not Found")
    void updateNotFound() {
        PostRequest request = new PostRequest();
        request.setTitle("Judul Update");
        request.setBody("Body Update");
        request.setComments(Arrays.asList(new Comment("Kenda 1 Update", "Body ke 1 Update"), new Comment("Kenda 2 Update", "Body ke 2 Update")));

        Mono<Post> mono = postService.update("NotFound", request);
        StepVerifier.create(mono)
                .expectErrorMatches(throwable -> throwable instanceof DataException && throwable.getMessage().equals("Data tidak ditemukan")
                ).verify();
    }

    @Test
    @Order(6)
    @DisplayName("Find Post")
    void read() {
        Flux<Post> flux = postService.find();

        // Belum nemu cara menggunakan StepVerifier dengan flux yang dinamis
        assertNotNull(flux.collectList().block());
    }

    @Test
    @Order(7)
    @DisplayName("Find Post by id")
    void findById() {
        Mono<Post> mono = postService.findById(postId);

        StepVerifier.create(mono)
                .consumeNextWith(post -> assertNotNull(post.getTitle()))
                .expectComplete()
                .verify();
    }

    @Test
    @Order(8)
    @DisplayName("Delete Post")
    void delete() {
        Mono<Post> mono = postService.delete(postId);

        StepVerifier.create(mono)
                .consumeNextWith(post -> assertEquals(postId, post.getId()))
                .expectComplete()
                .verify();
    }

    @Test
    @Order(9)
    @DisplayName("Delete Post Not Found")
    void deleteNotFound() {
        Mono<Post> mono = postService.delete(postId);

        StepVerifier.create(mono)
                .expectErrorMatches(throwable -> throwable instanceof DataException && throwable.getMessage().equals("Data tidak ditemukan")
                ).verify();
    }

    @Test
    @Order(10)
    @DisplayName("expectNext")
    void expectNext() {
        StepVerifier.create(Flux.just("Kenda", "Sukenda", "Kenda Sukenda"))
                .expectNext("Kenda")
                .expectNext("Sukenda")
                .expectNext("Kenda Sukenda")
                .expectComplete()
                .verify();

        StepVerifier.create(Mono.just("Kenda"))
                .expectNext("Kenda")
                .expectComplete()
                .verify();
    }
}