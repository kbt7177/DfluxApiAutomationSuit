package com.springernature.dflux.quality.webservice.config;

import com.springer.quality.api.model.ApiUserConfig;
import lombok.Data;

@Data
public class DfluxApiUserConfig {
    private ApiUserConfig thesUser;
    private ApiUserConfig journalsUser;
}
