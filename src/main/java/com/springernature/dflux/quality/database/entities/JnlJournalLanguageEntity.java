package com.springernature.dflux.quality.database.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "dbo.JnlJournalLanguage")
@NamedQuery(name = "JnlJournalLanguageEntity.getLanguageByJournalId", query = "select jnlJournalLanguageEntity from JnlJournalLanguageEntity as jnlJournalLanguageEntity where " +
        "jnlJournalLanguageEntity.journalId=:journalId")
public class JnlJournalLanguageEntity {
    @Column(name = "JournalId")
    private String journalId;

    @Id
    @Column(name = "LanguageId")
    private String languageId;
}
