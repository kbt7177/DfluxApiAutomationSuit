package com.springernature.dflux.quality.webservice.api.endpoints;

public enum ThesApiEndpoints {
    TERM_SEARCH("/filtered/term/search");

    private String endpoint;

    ThesApiEndpoints(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }
}
