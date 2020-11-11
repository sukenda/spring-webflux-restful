package com.kenda.webflux.restful.model;

import com.kenda.webflux.restful.entity.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class PostResponse {

    private String id;

    private String title;

    private String body;

    private List<Comment> comments;

}
