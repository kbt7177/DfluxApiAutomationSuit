package com.springernature.dflux.quality.database.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "dbo.JnlLanguage")
@NamedQuery(name = "JnlLanguageEntity.getLanguageByLanguageId", query = "select JnlLanguageEntity from JnlLanguageEntity as JnlLanguageEntity where " +
        "JnlLanguageEntity.languageId=:languageId")
public class JnlLanguageEntity {
    @Id
    @Column(name = "LanguageId")
    private String languageId;

    @Column(name = "version")
    private String version;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

}
