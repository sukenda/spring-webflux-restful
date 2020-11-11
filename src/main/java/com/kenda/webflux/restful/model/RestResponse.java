package com.kenda.webflux.restful.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RestResponse<T> {

    private String status;

    private int code;

    private T data;

}
