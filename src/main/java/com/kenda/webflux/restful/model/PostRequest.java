package com.kenda.webflux.restful.model;

import com.kenda.webflux.restful.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostRequest extends BaseModel {

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    private List<Comment> comments;

}
