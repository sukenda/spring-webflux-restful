package com.kenda.webflux.restful.model;

import com.kenda.webflux.restful.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PostResponse {

    private String id;

    private String title;

    private String body;

    private List<Comment> comments;

}
