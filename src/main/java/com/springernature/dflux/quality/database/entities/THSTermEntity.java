package com.springernature.dflux.quality.database.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "dbo.THSTerm")
@NamedQuery(name = "THSTermEntity.getThesTermRecordWhereLeadTermLike", query = "select tHSTermEntity from THSTermEntity as tHSTermEntity where " +
        "tHSTermEntity.leadTerm like :leadTerm")
@NamedQuery(name = "THSTermEntity.getThesTermRecordWhereLeadTerm", query = "select tHSTermEntity from THSTermEntity as tHSTermEntity where " +
        "tHSTermEntity.leadTerm=:leadTerm")
@NamedQuery(name = "THSTermEntity.getThesTermRecordsWithUseTerm", query = "select tHSTermEntity from THSTermEntity as tHSTermEntity where " +
        "tHSTermEntity.useTerm=:useTerm")
public class THSTermEntity {
    @Id
    @Column(name = "LeadTerm")
    private String leadTerm;

    @Column(name = "DisplayForm")
    private String displayForm;

    @Column(name = "IsApproved")
    private String isApproved;

    @Column(name = "DesType")
    private String desType;

    @Column(name = "UseTerm")
    private String useTerm;

    @Column(name = "ValidFor")
    private String validFor;

    @Column(name = "TermId")
    private String termId;

    @Column(name = "UseTermId")
    private String useTermId;
}
