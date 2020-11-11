package com.kenda.webflux.restful.model;

import com.kenda.webflux.restful.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostResponse extends BaseModel {

    private String id;

    private String title;

    private String body;

    private List<Comment> comments;

}
