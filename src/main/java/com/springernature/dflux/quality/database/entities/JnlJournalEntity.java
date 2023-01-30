package com.springernature.dflux.quality.database.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "dbo.JnlJournal")
@NamedQuery(name = "JnlJournalEntity.getJournalForWiproId", query = "select jnlJournalEntity from JnlJournalEntity as jnlJournalEntity where " +
        "jnlJournalEntity.wiproJournalId=:wiproJournalId and isActive='1'")
public class JnlJournalEntity {
    @Id
    @Column(name = "JournalId")
    private String journalId;

    @Column(name = "version")
    private String version;

    @Column(name = "description")
    private String description;

    @Column(name = "title")
    private String title;

    @Column(name = "abbreviation")
    private String abbreviation;

    @Column(name = "issn")
    private String issn;

    @Column(name = "electronicIssn")
    private String electronicIssn;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "isActive")
    private String isActive;

    @Column(name = "CountryId")
    private String countryId;


    @Column(name = "WiproJournalId")
    private String wiproJournalId;

}
