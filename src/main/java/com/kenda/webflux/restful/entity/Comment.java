package com.kenda.webflux.restful.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Comment implements Serializable {

    private String name;

    private String body;

}
