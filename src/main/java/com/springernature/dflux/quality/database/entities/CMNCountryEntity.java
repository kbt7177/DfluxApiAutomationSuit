package com.springernature.dflux.quality.database.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "dbo.CMNCountry")
@NamedQuery(name = "CMNCountryEntity.getCountryRecordByCountryId", query = "select cmnCountryEntity from CMNCountryEntity as cmnCountryEntity where " +
        "cmnCountryEntity.countryId=:countryId")
@NamedQuery(name = "CMNCountryEntity.getCountryRecordByCountry", query = "select cmnCountryEntity from CMNCountryEntity as cmnCountryEntity where " +
        "cmnCountryEntity.country=:country")
public class CMNCountryEntity {
    @Id
    @Column(name = "CountryId")
    private String countryId;

    @Column(name = "CountryCode")
    private String countryCode;

    @Column(name = "Country")
    private String country;

    @Column(name = "ParentId")
    private String parentId;

    @Column(name = "CountryTypeId")
    private String countryTypeId;
}
