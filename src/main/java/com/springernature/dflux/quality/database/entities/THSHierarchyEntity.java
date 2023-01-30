package com.springernature.dflux.quality.database.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "dbo.THSHierarchy")
@IdClass(THSCompositKey.class)
@NamedQuery(name = "THSHierarchyEntity.getThesHierarcyRecordByLeadTerm", query = "select tHSHierarchyEntity from THSHierarchyEntity as tHSHierarchyEntity where " +
        "tHSHierarchyEntity.leadTerm=:leadTerm")
@NamedQuery(name = "THSHierarchyEntity.getThesHierarcyRecordByParentTerm", query = "select tHSHierarchyEntity from THSHierarchyEntity as tHSHierarchyEntity where " +
        "tHSHierarchyEntity.parentTerm=:parentTerm")
public class THSHierarchyEntity {

    @Column(name = "RelCode")
    private String relCode;

    @Id
    @Column(name = "LeadTerm")
    private String leadTerm;

    @Id
    @Column(name = "ParentTerm")
    private String parentTerm;

    @Column(name = "Hierarchy")
    private String hierarchy;

}
