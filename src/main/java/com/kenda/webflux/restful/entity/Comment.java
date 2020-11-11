package com.kenda.webflux.restful.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {

    private String name;

    private String body;

}
