package com.springernature.dflux.quality.database.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "dbo.THSGroupTerm")
@IdClass(THSCompositKey.class)
@NamedQuery(name = "THSGroupTermEntity.getTHSGroupTermRecordByLeadTerm", query = "select tHSGroupTermEntity from THSGroupTermEntity as tHSGroupTermEntity where " +
        "tHSGroupTermEntity.leadTerm=:leadTerm")
@NamedQuery(name = "THSGroupTermEntity.getTHSGroupTermRecordByParentTerm", query = "select tHSGroupTermEntity from THSGroupTermEntity as tHSGroupTermEntity where " +
        "tHSGroupTermEntity.parentTerm=:parentTerm")
public class THSGroupTermEntity {

    @Column(name = "RelCode")
    private String relCode;

    @Id
    @Column(name = "LeadTerm")
    private String leadTerm;

    @Id
    @Column(name = "ParentTerm")
    private String parentTerm;
}
