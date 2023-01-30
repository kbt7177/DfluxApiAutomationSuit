package com.springernature.dflux.quality.api.dtos.journalsapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JournalsApiResponse {
    @JsonProperty("status")
    private String status;
}
