package com.kenda.webflux.restful.model;

import com.kenda.webflux.restful.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PostRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    private List<Comment> comments;

}
