package com.springernature.dflux.quality.database.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "dbo.JnlOrganisation")
@NamedQuery(name = "JnlOrganisationEntity.getJnlOrganizationRecordByOrganisationId", query = "select jnlOrganisationEntity from JnlOrganisationEntity as jnlOrganisationEntity where " +
        "jnlOrganisationEntity.organisationId=:organisationId")
public class JnlOrganisationEntity {
    @Id
    @Column(name = "OrganisationId")
    private String organisationId;

    @Column(name = "version")
    private String version;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "comment")
    private String comment;

    @Column(name = "ParentId")
    private String parentId;
}
