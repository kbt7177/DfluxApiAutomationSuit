package com.springernature.dflux.quality.database.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "dbo.JnlImprint")
@NamedQuery(name = "JnlImprintEntity.getImprintByJournalId", query = "select jnlImprintEntity from JnlImprintEntity as jnlImprintEntity where " +
        "jnlImprintEntity.journalId=:journalId")
public class JnlImprintEntity {
    @Id
    @Column(name = "ImprintId")
    private String imprintId;

    @Column(name = "version")
    private String version;

    @Column(name = "sequenceId")
    private String sequenceId;

    @Column(name = "city")
    private String city;

    @Column(name = "startYear")
    private String startYear;

    @Column(name = "endYear")
    private String endYear;

    @Column(name = "comment")
    private String comment;

    @Column(name = "publisherId")
    private String publisherId;

    @Column(name = "CountryId")
    private String countryId;

    @Column(name = "JournalId")
    private String journalId;

}
