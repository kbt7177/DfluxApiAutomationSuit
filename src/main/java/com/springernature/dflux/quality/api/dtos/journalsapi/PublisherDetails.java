package com.springernature.dflux.quality.api.dtos.journalsapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Comparator;

@Data
public class PublisherDetails {
    @JsonProperty("PublisherCountry")
    private String publisherCountry;

    @JsonProperty("Publisher")
    private String publisher;
}
