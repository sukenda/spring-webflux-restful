package com.kenda.webflux.restful.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Document(value = "posts")
public class Post extends BaseEntity {

    @Indexed
    private String title;

    private String body;

    private List<Comment> comments;

}
