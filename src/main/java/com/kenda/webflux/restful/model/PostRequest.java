package com.kenda.webflux.restful.model;

import com.kenda.webflux.restful.entity.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@NoArgsConstructor
@Data
public class PostRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    private List<Comment> comments;

}
