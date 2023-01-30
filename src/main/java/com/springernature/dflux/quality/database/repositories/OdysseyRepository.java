package com.springernature.dflux.quality.database.repositories;

import com.springer.quality.database.item.DatabaseItem;
import com.springer.quality.database.model.DatabaseConfig;
import com.springer.quality.database.repository.BaseRepository;
import com.springernature.dflux.quality.database.config.DatabaseConfigurations;
import com.springernature.dflux.quality.database.entities.*;

import javax.persistence.TypedQuery;
import java.util.List;

public class OdysseyRepository extends BaseRepository {


    public JnlJournalEntity findJournalRecord(String wiproJournalId) {
        return execute(entityManager -> {
            TypedQuery<JnlJournalEntity> query = entityManager.createNamedQuery("JnlJournalEntity.getJournalForWiproId", JnlJournalEntity.class);
            query.setParameter("wiproJournalId", wiproJournalId);
            return query.getSingleResult();
        });
    }

    public CMNCountryEntity findCMNCountryRecordByCountryId(String countryId) {
        return execute(entityManager -> {
            TypedQuery<CMNCountryEntity> query = entityManager.createNamedQuery("CMNCountryEntity.getCountryRecordByCountryId", CMNCountryEntity.class);
            query.setParameter("countryId", countryId);
            return query.getSingleResult();
        });
    }

    public CMNCountryEntity findCMNCountryRecordByCountry(String country) {
        return execute(entityManager -> {
            TypedQuery<CMNCountryEntity> query = entityManager.createNamedQuery("CMNCountryEntity.getCountryRecordByCountry", CMNCountryEntity.class);
            query.setParameter("country", country);
            return query.getSingleResult();
        });
    }

    public List<JnlJournalLanguageEntity> findJournalLanguageRecordByJournalId(String journalId) {
        return execute(entityManager -> {
            TypedQuery<JnlJournalLanguageEntity> query = entityManager.createNamedQuery("JnlJournalLanguageEntity.getLanguageByJournalId", JnlJournalLanguageEntity.class);
            query.setParameter("journalId", journalId);
            return query.getResultList();
        });
    }

    public JnlLanguageEntity findLanguageRecordByLanguageId(String languageId) {
        return execute(entityManager -> {
            TypedQuery<JnlLanguageEntity> query = entityManager.createNamedQuery("JnlLanguageEntity.getLanguageByLanguageId", JnlLanguageEntity.class);
            query.setParameter("languageId", languageId);
            return query.getSingleResult();
        });
    }

    public List<JnlImprintEntity> findImprintsRecordByJournalId(String journalId) {
        return execute(entityManager -> {
            TypedQuery<JnlImprintEntity> query = entityManager.createNamedQuery("JnlImprintEntity.getImprintByJournalId", JnlImprintEntity.class);
            query.setParameter("journalId", journalId);
            return query.getResultList();
        });
    }

    public JnlOrganisationEntity findJnlOrganisationRecordByOrganisationId(String organisationId) {
        return execute(entityManager -> {
            TypedQuery<JnlOrganisationEntity> query = entityManager.createNamedQuery("JnlOrganisationEntity.getJnlOrganizationRecordByOrganisationId", JnlOrganisationEntity.class);
            query.setParameter("organisationId", organisationId);
            return query.getSingleResult();
        });
    }

    public List<THSTermEntity> findThesTermRecordsByLeadTermLike(String leadTerm) {
        return execute(entityManager -> {
            TypedQuery<THSTermEntity> query = entityManager.createNamedQuery("THSTermEntity.getThesTermRecordWhereLeadTermLike", THSTermEntity.class);
            query.setParameter("leadTerm", "%" + leadTerm + "%");
            return query.getResultList();
        });
    }

    public List<THSTermEntity> findThesTermRecordsByLeadTerm(String leadTerm) {
        return execute(entityManager -> {
            TypedQuery<THSTermEntity> query = entityManager.createNamedQuery("THSTermEntity.getThesTermRecordWhereLeadTerm", THSTermEntity.class);
            query.setParameter("leadTerm", leadTerm);
            return query.getResultList();
        });
    }

    public List<THSTermEntity> findThesTermRecordsByUseTerm(String useTerm) {
        return execute(entityManager -> {
            TypedQuery<THSTermEntity> query = entityManager.createNamedQuery("THSTermEntity.getThesTermRecordsWithUseTerm", THSTermEntity.class);
            query.setParameter("useTerm", useTerm);
            return query.getResultList();
        });
    }


    public List<THSHierarchyEntity> findThesHierarcyRecordsByLeadTerm(String leadTerm) {
        return execute(entityManager -> {
            TypedQuery<THSHierarchyEntity> query = entityManager.createNamedQuery("THSHierarchyEntity.getThesHierarcyRecordByLeadTerm", THSHierarchyEntity.class);
            query.setParameter("leadTerm", leadTerm);
            return query.getResultList();
        });
    }

    public List<THSHierarchyEntity> findThesHierarcyRecordsByParentTerm(String parentTerm) {
        return execute(entityManager -> {
            TypedQuery<THSHierarchyEntity> query = entityManager.createNamedQuery("THSHierarchyEntity.getThesHierarcyRecordByParentTerm", THSHierarchyEntity.class);
            query.setParameter("parentTerm", parentTerm);
            return query.getResultList();
        });
    }

    public List<THSGroupTermEntity> findThesGroupTermRecordsByLeadTerm(String leadTerm) {
        return execute(entityManager -> {
            TypedQuery<THSGroupTermEntity> query = entityManager.createNamedQuery("THSGroupTermEntity.getTHSGroupTermRecordByLeadTerm", THSGroupTermEntity.class);
            query.setParameter("leadTerm", leadTerm);
            return query.getResultList();
        });
    }

    public List<THSGroupTermEntity> findThesGroupTermRecordsByParentTerm(String parentTerm) {
        return execute(entityManager -> {
            TypedQuery<THSGroupTermEntity> query = entityManager.createNamedQuery("THSGroupTermEntity.getTHSGroupTermRecordByParentTerm", THSGroupTermEntity.class);
            query.setParameter("parentTerm", parentTerm);
            return query.getResultList();
        });
    }

    public List<THSRelatedTermEntity> findThesRelatedTermRecordsByLeadTerm(String leadTerm) {
        return execute(entityManager -> {
            TypedQuery<THSRelatedTermEntity> query = entityManager.createNamedQuery("THSRelatedTermEntity.getRelatedTermRecordsByLeadTerm", THSRelatedTermEntity.class);
            query.setParameter("leadTerm", leadTerm);
            return query.getResultList();
        });
    }


    @Override
    protected DatabaseConfig getConfig() {
        return DatabaseItem.instance().getConfig(DatabaseConfigurations.class).getOdysseyDatabase();
    }
}
