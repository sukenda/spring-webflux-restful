package com.kenda.webflux.restful.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "posts")
public class Post extends BaseEntity {

    @Indexed
    private String title;

    private String body;

    private List<Comment> comments;

}
