package com.springernature.dflux.quality.webservice.config;

import com.springer.quality.api.model.ApiConfig;
import lombok.Data;

@Data
public class DfluxApiConfig {
    private ApiConfig thesApi;
    private ApiConfig journalsApi;
}

