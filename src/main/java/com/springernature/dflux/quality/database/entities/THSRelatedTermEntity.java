package com.springernature.dflux.quality.database.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "dbo.THSRelatedTerm")
@IdClass(THSRelatedTermCompositKey.class)
@NamedQuery(name = "THSRelatedTermEntity.getRelatedTermRecordsByLeadTerm", query = "select tHSRelatedTermEntity from THSRelatedTermEntity as tHSRelatedTermEntity where " +
        "tHSRelatedTermEntity.leadTerm=:leadTerm")
public class THSRelatedTermEntity {

    @Id
    @Column(name = "LeadTerm")
    private String leadTerm;

    @Id
    @Column(name = "RelatedTerm")
    private String relatedTerm;

}
