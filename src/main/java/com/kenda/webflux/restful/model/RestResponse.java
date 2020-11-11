package com.kenda.webflux.restful.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestResponse<T> {

    private String status;

    private int code;

    private T data;

}
