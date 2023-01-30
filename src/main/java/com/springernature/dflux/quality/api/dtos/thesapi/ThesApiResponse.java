package com.springernature.dflux.quality.api.dtos.thesapi;

import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
public class ThesApiResponse implements Comparable<ThesApiResponse>{
    private List<String> altLabels = new ArrayList<>();
    private List<String> broader = new ArrayList<>();
    private List<String> narrower = new ArrayList<>();
    private List<String> drugClasses = new ArrayList<>();
    private String normalizedLabel;
    private String prefLabel;
    private List<String> related = new ArrayList<>();
    private List<String> thesIds = new ArrayList<>();
    private String uri;
    private List<String> vocabularyUris = new ArrayList<>();

    @Override
    public int compareTo(ThesApiResponse thesApiResponse) {
        return this.normalizedLabel.compareTo(thesApiResponse.normalizedLabel);
    }
}
