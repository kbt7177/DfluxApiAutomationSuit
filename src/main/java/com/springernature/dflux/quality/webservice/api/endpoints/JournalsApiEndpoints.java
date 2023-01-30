package com.springernature.dflux.quality.webservice.api.endpoints;

public enum JournalsApiEndpoints {
    UPDATE("/api/journalservice/journal/wiproJournalsId/update"),
    DELETE("/api/journalservice/journal/wiproJournalsId/delete");

    private String endpoint;

    JournalsApiEndpoints(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint(Integer wiproJournalId) {
        String str = endpoint.replaceAll("wiproJournalsId", String.valueOf(wiproJournalId));
        return str;
    }
}
