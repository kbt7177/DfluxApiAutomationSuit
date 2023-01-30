package com.springernature.dflux.quality.test;

import com.springer.quality.api.item.ApiUserItem;
import com.springer.quality.api.response.ListResponse;
import com.springer.quality.utilities.testnglisteners.CustomeITest;
import com.springernature.dflux.quality.api.dtos.request.ApiRequest;
import com.springernature.dflux.quality.api.dtos.thesapi.ThesApiResponse;
import com.springernature.dflux.quality.database.entities.THSGroupTermEntity;
import com.springernature.dflux.quality.database.entities.THSHierarchyEntity;
import com.springernature.dflux.quality.database.entities.THSRelatedTermEntity;
import com.springernature.dflux.quality.database.entities.THSTermEntity;
import com.springernature.dflux.quality.database.repositories.OdysseyRepository;
import com.springernature.dflux.quality.dataprovider.thesapi.ThesApiDataProvider;
import com.springernature.dflux.quality.webservice.api.ThesApi;
import com.springernature.dflux.quality.webservice.config.DfluxApiUserConfig;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ThesApiTest implements CustomeITest {

    private final OdysseyRepository repository = new OdysseyRepository();
    private static final DfluxApiUserConfig user = ApiUserItem.instance().getConfig(DfluxApiUserConfig.class);
    private ThesApi api = new ThesApi(user.getThesUser());
    private final String staticString = "http://km.springernature.com/adis/";
    private List<String> acceptedDeskType = Arrays.asList("DC_B", "DC_C", "DC_M", "DC_T", "DEV", "DIS", "DRUG", "GEN", "NDIS");

    @Test(dataProvider = "getThesApiRequestDataForFailureScenario", dataProviderClass = ThesApiDataProvider.class)
    public void verifyThesApiForNegativeScenarios(String runMode, String testName, ApiRequest request, Integer responseStatusCode, String message) {
        try {
            if (!"y".equalsIgnoreCase(runMode)) {
                throw new SkipException("Run Mode for test case is OFF, Skipping the test");
            }
            ListResponse<ThesApiResponse> response = api.getThesTerm(request);
            Assert.assertEquals(response.getResponse().statusCode(), responseStatusCode.intValue(), "Response Status code is not matching");
            if (response.getResponse().getBody().jsonPath().get("message") != null) {
                Assert.assertEquals(response.getResponse().getBody().jsonPath().get("message").toString(), message, "Incorrect Message returned for failure scenarios");
            }
        } catch (SkipException e) {
            log.warn("Skipping the test : ", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Exception in test execution : ", e);
            Assert.fail("Exception in Test execution", e);
        }
    }

    @Test(dataProvider = "getThesApiRequestDataForSuccessScenario", dataProviderClass = ThesApiDataProvider.class)
    public void verifyThesApiForSuccessScenarios(String runMode, String testName, ApiRequest request, Integer responseStatusCode) {
        try {
            if (!"y".equalsIgnoreCase(runMode)) {
                throw new SkipException("Run Mode for test case is OFF, Skipping the test");
            }
            ListResponse<ThesApiResponse> response = api.getThesTerm(request);
            Assert.assertEquals(response.getResponse().statusCode(), responseStatusCode.intValue(), "Response Status code is not matching");
            verifyThesResponse(request, response);
            log.info("Test passed");
        } catch (SkipException e) {
            log.warn("Skipping the test : ", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Exception in test execution : ", e);
            Assert.fail("Exception in Test execution", e);
        }
    }


    private void verifyThesResponse(ApiRequest request, ListResponse<ThesApiResponse> response) {
        log.info("Database Validation started for the request");
        ListResponse<ThesApiResponse> expectedResponseList = new ListResponse<>();
        List<ThesApiResponse> expectedResponse = new ArrayList<>();

        boolean altLable = Boolean.valueOf(request.getQueryParams().get("includeAltLabels").toString());
        boolean caseSensitive = !Boolean.valueOf(request.getQueryParams().get("caseInsensitive").toString());
        boolean subString = Boolean.valueOf(request.getQueryParams().get("substring").toString());

        List<THSTermEntity> thsTermEntityList = null;

        if (subString) {
            log.debug("SubString is true, so fetching all records");
            if (caseSensitive) {
                log.debug("Case Sensitive is true so updating records matching to case sensitivity");
                thsTermEntityList = repository.findThesTermRecordsByLeadTermLike(request.getQueryParams().get("label").toString()).stream().filter(x -> x.getLeadTerm().contains(request.getQueryParams().get("label").toString())).collect(Collectors.toList());
            } else {
                log.debug("Case Sensitive is false so taking all the records matching the criteria");
                thsTermEntityList = repository.findThesTermRecordsByLeadTermLike(request.getQueryParams().get("label").toString());
            }
        } else {
            log.debug("SubString is false, so fetching records matching with label");
            if (caseSensitive) {
                log.debug("Case Sensitive is true so updating records matching to case sensitivity");
                thsTermEntityList = repository.findThesTermRecordsByLeadTerm(request.getQueryParams().get("label").toString()).stream().filter(x -> x.getLeadTerm().contains(request.getQueryParams().get("label").toString())).collect(Collectors.toList());
            } else {
                log.debug("Case Sensitive is false so taking all the records matching the criteria");
                thsTermEntityList = repository.findThesTermRecordsByLeadTerm(request.getQueryParams().get("label").toString());
            }
        }

        if (!altLable) {
            log.debug("Include Alt Label is false so updating DB list");
            thsTermEntityList = thsTermEntityList.stream().filter(x -> x.getDisplayForm() != null).collect(Collectors.toList());
        }

        log.debug("Updating DB list for allowed desType records");
        thsTermEntityList = thsTermEntityList.stream().filter(x -> acceptedDeskType.contains(x.getDesType())).collect(Collectors.toList());

//        System.out.println("DB records " + thsTermEntityList);

        for (THSTermEntity thsTermEntity : thsTermEntityList) {
            ThesApiResponse expectedThesObject = null;
            if (thsTermEntity.getDisplayForm() != null) {
                log.debug("displayForm is not null for Lead Term : " + thsTermEntity.getLeadTerm());
                if (!expectedResponse.isEmpty() && expectedResponse.stream().filter(x -> x.getNormalizedLabel().equals(thsTermEntity.getLeadTerm())).findFirst().isPresent()) {
                    log.debug("Record already added as object so skipping it");
                    continue;
                } else {
                    log.debug("Record not already added as object so creating new one");
                    expectedThesObject = new ThesApiResponse();
                }
                expectedThesObject.setPrefLabel(thsTermEntity.getDisplayForm());
                expectedThesObject.setNormalizedLabel(thsTermEntity.getLeadTerm());
                expectedThesObject.getThesIds().add(thsTermEntity.getTermId());
                expectedThesObject.setUri(staticString + thsTermEntity.getTermId());
                addDrugclassBroaderAndNarrower(expectedThesObject, thsTermEntity.getDesType(), thsTermEntity.getLeadTerm());

                log.debug("Adding all altLabels");
                for (THSTermEntity thsTermEntity1 : repository.findThesTermRecordsByUseTerm(thsTermEntity.getLeadTerm())) {
                    expectedThesObject.getAltLabels().add(thsTermEntity1.getLeadTerm());
                }
                log.debug("Adding all related objects");
                for (THSRelatedTermEntity thsRelatedTermEntity : repository.findThesRelatedTermRecordsByLeadTerm(thsTermEntity.getLeadTerm())) {
                    expectedThesObject.getRelated().add(staticString + repository.findThesTermRecordsByLeadTerm(thsRelatedTermEntity.getRelatedTerm()).get(0).getTermId());
                }
                addVocabularies(thsTermEntity, expectedThesObject);
            } else {
                log.debug("displayForm is null for Lead Term : " + thsTermEntity.getLeadTerm());
                if (!expectedResponse.isEmpty() && expectedResponse.stream().filter(x -> x.getNormalizedLabel().equals(thsTermEntity.getUseTerm())).findAny().isPresent()) {
                    log.debug("Record already added as object so skipping it");
                    continue;
                } else {
                    log.debug("Record not already added as object so creating new one");
                    expectedThesObject = new ThesApiResponse();
                }
                THSTermEntity record = repository.findThesTermRecordsByLeadTerm(thsTermEntity.getUseTerm()).get(0);
                expectedThesObject.setPrefLabel(record.getDisplayForm());
                expectedThesObject.setNormalizedLabel(record.getLeadTerm());
                expectedThesObject.getThesIds().add(record.getTermId());
                expectedThesObject.setUri(staticString + record.getTermId());
                addDrugclassBroaderAndNarrower(expectedThesObject, thsTermEntity.getDesType(), thsTermEntity.getUseTerm());

                log.debug("Adding all altLabels");
                for (THSTermEntity thsTermEntity1 : repository.findThesTermRecordsByUseTerm(thsTermEntity.getUseTerm())) {
                    expectedThesObject.getAltLabels().add(thsTermEntity1.getLeadTerm());
                }
                log.debug("Adding all related objects");
                for (THSRelatedTermEntity thsRelatedTermEntity : repository.findThesRelatedTermRecordsByLeadTerm(thsTermEntity.getUseTerm())) {
                    expectedThesObject.getRelated().add(staticString + repository.findThesTermRecordsByLeadTerm(thsRelatedTermEntity.getRelatedTerm()).get(0).getTermId());
                }
                addVocabularies(thsTermEntity, expectedThesObject);
            }
            expectedResponse.add(expectedThesObject);
        }

        log.debug("Sorting Expected object");
        Collections.sort(expectedResponse);
        List<ThesApiResponse> actualResponse = new ArrayList<>(response.getBody());
        log.debug("Sorting Actual object");
        Collections.sort(actualResponse);
        log.info("Verify size of both Objects");
        Assert.assertEquals(actualResponse.size(), expectedResponse.size(), "Not all object match in actual and expected response");
        log.info("Actual and Expected object list matched");

        log.info("Verify each object");
        for (int i = 0; i < expectedResponse.size(); i++) {
            ThesApiResponse actualResponseObject = actualResponse.get(i);
            Collections.sort(actualResponseObject.getAltLabels());
            Collections.sort(actualResponseObject.getNarrower());
            Collections.sort(actualResponseObject.getBroader());
            Collections.sort(actualResponseObject.getDrugClasses());
            Collections.sort(actualResponseObject.getRelated());
            Collections.sort(actualResponseObject.getThesIds());
            Collections.sort(actualResponseObject.getVocabularyUris());

            ThesApiResponse expectedResponseObject = expectedResponse.get(i);
            Collections.sort(expectedResponseObject.getAltLabels());
            Collections.sort(expectedResponseObject.getNarrower());
            Collections.sort(expectedResponseObject.getBroader());
            Collections.sort(expectedResponseObject.getDrugClasses());
            Collections.sort(expectedResponseObject.getRelated());
            Collections.sort(expectedResponseObject.getThesIds());
            Collections.sort(expectedResponseObject.getVocabularyUris());
            Assert.assertEquals(actualResponseObject, expectedResponseObject, "Validation failed for the object : " + actualResponseObject);
        }
        log.info("All Objects are matched correctly");
    }

    private void addDrugclassBroaderAndNarrower(ThesApiResponse expectedThesObject, String desType, String thesTerm) {
        if (desType.equals("DRUG")) {
            log.debug("Adding drug classes");
            for (THSHierarchyEntity thsHierarchyEntity : repository.findThesHierarcyRecordsByLeadTerm(thesTerm)) {
                THSTermEntity record = repository.findThesTermRecordsByLeadTerm(thsHierarchyEntity.getParentTerm()).get(0);
                if (acceptedDeskType.contains(record.getDesType()))
                    expectedThesObject.getDrugClasses().add(staticString + record.getTermId());
            }
            for (THSHierarchyEntity thsHierarchyEntity : repository.findThesHierarcyRecordsByParentTerm(thesTerm)) {
                THSTermEntity record = repository.findThesTermRecordsByLeadTerm(thsHierarchyEntity.getLeadTerm()).get(0);
                if (acceptedDeskType.contains(record.getDesType()))
                    expectedThesObject.getDrugClasses().add(staticString + record.getTermId());
            }
            for (THSGroupTermEntity thsGroupTermEntity : repository.findThesGroupTermRecordsByLeadTerm(thesTerm)) {
                THSTermEntity record = repository.findThesTermRecordsByLeadTerm(thsGroupTermEntity.getParentTerm()).get(0);
                if (acceptedDeskType.contains(record.getDesType()))
                    expectedThesObject.getDrugClasses().add(staticString + record.getTermId());
            }
            for (THSGroupTermEntity thsGroupTermEntity : repository.findThesGroupTermRecordsByParentTerm(thesTerm)) {
                THSTermEntity record = repository.findThesTermRecordsByLeadTerm(thsGroupTermEntity.getLeadTerm()).get(0);
                if (acceptedDeskType.contains(record.getDesType()))
                    expectedThesObject.getDrugClasses().add(staticString + record.getTermId());
            }
        } else {
            log.debug("Adding Broader and Narrower");
            for (THSHierarchyEntity thsHierarchyEntity : repository.findThesHierarcyRecordsByLeadTerm(thesTerm).stream().filter(x -> !x.getRelCode().equals("DRGB")).collect(Collectors.toList())) {
                THSTermEntity record = repository.findThesTermRecordsByLeadTerm(thsHierarchyEntity.getParentTerm()).get(0);
                if (acceptedDeskType.contains(record.getDesType()))
                    expectedThesObject.getBroader().add(staticString + record.getTermId());
            }
            for (THSHierarchyEntity thsHierarchyEntity : repository.findThesHierarcyRecordsByParentTerm(thesTerm).stream().filter(x -> !x.getRelCode().equals("DRGB")).collect(Collectors.toList())) {
                THSTermEntity record = repository.findThesTermRecordsByLeadTerm(thsHierarchyEntity.getLeadTerm()).get(0);
                if (acceptedDeskType.contains(record.getDesType()))
                    expectedThesObject.getNarrower().add(staticString + record.getTermId());
            }
            for (THSGroupTermEntity thsGroupTermEntity : repository.findThesGroupTermRecordsByLeadTerm(thesTerm).stream().filter(x -> !x.getRelCode().equals("DRGB")).collect(Collectors.toList())) {
                THSTermEntity record = repository.findThesTermRecordsByLeadTerm(thsGroupTermEntity.getParentTerm()).get(0);
                if (acceptedDeskType.contains(record.getDesType()))
                    expectedThesObject.getBroader().add(staticString + record.getTermId());
            }
            for (THSGroupTermEntity thsGroupTermEntity : repository.findThesGroupTermRecordsByParentTerm(thesTerm).stream().filter(x -> !x.getRelCode().equals("DRGB")).collect(Collectors.toList())) {
                THSTermEntity record = repository.findThesTermRecordsByLeadTerm(thsGroupTermEntity.getLeadTerm()).get(0);
                if (acceptedDeskType.contains(record.getDesType()))
                    expectedThesObject.getNarrower().add(staticString + record.getTermId());
            }
        }
    }

    private void addVocabularies(THSTermEntity record, ThesApiResponse expectedThesObject) {
        log.debug("Adding vocabularies");
        switch (record.getDesType()) {
            case "DC_M":
                expectedThesObject.getVocabularyUris().add(staticString + "DrugClass");
                expectedThesObject.getVocabularyUris().add(staticString + "DrugClassMechanismOfAction");
                expectedThesObject.getVocabularyUris().add(staticString + "DrugOrDrugClass");
                break;
            case "DRUG":
                expectedThesObject.getVocabularyUris().add(staticString + "Drug");
                expectedThesObject.getVocabularyUris().add(staticString + "DrugOrDrugClass");
                break;
            case "DC_B":
                expectedThesObject.getVocabularyUris().add(staticString + "DrugClass");
                expectedThesObject.getVocabularyUris().add(staticString + "DrugClassBiological");
                expectedThesObject.getVocabularyUris().add(staticString + "DrugOrDrugClass");
                break;
            case "DIS":
                expectedThesObject.getVocabularyUris().add(staticString + "Disease");
                break;
            case "DC_C":
                expectedThesObject.getVocabularyUris().add(staticString + "DrugClass");
                expectedThesObject.getVocabularyUris().add(staticString + "DrugClassChemical");
                expectedThesObject.getVocabularyUris().add(staticString + "DrugOrDrugClass");
                break;
            case "DC_T":
                expectedThesObject.getVocabularyUris().add(staticString + "DrugClass");
                expectedThesObject.getVocabularyUris().add(staticString + "DrugClassTherapeuticEffect");
                expectedThesObject.getVocabularyUris().add(staticString + "DrugOrDrugClass");
                break;
            case "DEV":
                expectedThesObject.getVocabularyUris().add(staticString + "Device");
                break;
            case "GEN":
                expectedThesObject.getVocabularyUris().add(staticString + "General");
                expectedThesObject.getVocabularyUris().add(staticString + "SpecialInterestTerm");
                break;
            case "NDIS":
                expectedThesObject.getVocabularyUris().add(staticString + "NonDiseaseState");
                break;
            default:
                log.info("Vocabularies won't add as DesType is not in allowed list");
        }
    }


    //@Test
    public void sampleTest() {


    }
}
