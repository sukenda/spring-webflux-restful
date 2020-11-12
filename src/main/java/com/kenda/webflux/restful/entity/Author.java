package com.kenda.webflux.restful.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by sukenda
 **/
@Getter
@Setter
@NoArgsConstructor
@Document(collection = "authors")
public class Author extends BaseEntity {

    @TextIndexed
    private String firstName;

    @TextIndexed
    private String lastName;

    private String city;

    private String gender;

}
