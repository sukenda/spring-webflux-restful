package com.kenda.webflux.restful.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RestResponse<T> {

    private String status;

    private int code;

    private T data;

    private Integer rows;

}
