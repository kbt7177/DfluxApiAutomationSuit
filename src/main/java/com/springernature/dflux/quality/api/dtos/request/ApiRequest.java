package com.springernature.dflux.quality.api.dtos.request;

import lombok.Data;

import java.util.Map;

@Data
public class ApiRequest<T> {

    private Map<String, ?> headers;
    private Map<String, ?> queryParams;
    private T body;
}
