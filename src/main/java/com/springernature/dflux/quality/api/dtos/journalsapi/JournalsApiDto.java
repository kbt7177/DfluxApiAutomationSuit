package com.springernature.dflux.quality.api.dtos.journalsapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import java.util.List;

@Data
public class JournalsApiDto {
    @JsonProperty("Title")
    private String title;

    @JsonProperty("Abbreviation")
    private String abbreviation;

    @JsonProperty("ISSN")
    private String issn;

    @JsonProperty("ElectronicISSN")
    private String electronicISSN;

    @JsonProperty("JournalCountry")
    private String journalCountry;

    @JsonProperty("Language")
    private String language;

    @JsonProperty("PublisherDetails")
    private List<PublisherDetails> publisherDetails;

    @JsonProperty("JournalUrl")
    private String journalUrl;
}
