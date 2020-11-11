package com.kenda.webflux.restful.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> map = super.getErrorAttributes(request, options);
        Throwable error = getError(request);
        if (error instanceof ResponseStatusException) {
            map.put("status", ((ResponseStatusException) error).getStatus().value());
            map.put("message", ((ResponseStatusException) error).getReason());
        } else {
            map.put("status", HttpStatus.BAD_REQUEST.value());
            map.put("code", HttpStatus.BAD_GATEWAY.value());
            map.put("message", error.getMessage());
        }

        return map;
    }
}
