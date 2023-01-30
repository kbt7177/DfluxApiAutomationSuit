package com.springernature.dflux.quality.test;

import com.springer.quality.api.item.ApiUserItem;
import com.springer.quality.api.response.ModelResponse;
import com.springer.quality.utilities.exceptions.ReadExcelException;
import com.springer.quality.utilities.testnglisteners.CustomeITest;
import com.springernature.dflux.quality.api.dtos.journalsapi.JournalsApiDto;
import com.springernature.dflux.quality.api.dtos.journalsapi.JournalsApiResponse;
import com.springernature.dflux.quality.api.dtos.journalsapi.PublisherDetails;
import com.springernature.dflux.quality.api.dtos.request.ApiRequest;
import com.springernature.dflux.quality.database.entities.*;
import com.springernature.dflux.quality.database.repositories.OdysseyRepository;
import com.springernature.dflux.quality.dataprovider.journalsapi.JournalsApiDataProvider;
import com.springernature.dflux.quality.webservice.api.JournalsApi;
import com.springernature.dflux.quality.webservice.config.DfluxApiUserConfig;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.Test;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.*;

@Slf4j
public class JournalsApiTests implements CustomeITest {

    private static final DfluxApiUserConfig user = ApiUserItem.instance().getConfig(DfluxApiUserConfig.class);
    private final OdysseyRepository repository = new OdysseyRepository();

    private JournalsApi api = new JournalsApi(user.getJournalsUser());


    @Test(priority = 0, dataProviderClass = JournalsApiDataProvider.class, dataProvider = "getJournalsApiObjectForFailScenarios")
    public void verifyJournalsApiForFailureScenarios(String runMode, String testName, Integer wiproJournalsId, ApiRequest request, String expectedErrorMessage) {
        try {
            if (!runMode.equalsIgnoreCase("y")) {
                throw new SkipException("Test case run mode is OFF, so skipping the test");
            }
            ModelResponse<JournalsApiResponse> response = api.updateJournalsApi(wiproJournalsId, request);
            if (response.getResponse().getBody().jsonPath().get("message") != null) {
                Assert.assertEquals(response.getResponse().getBody().jsonPath().get("message").toString(), expectedErrorMessage, "Incorrect Message returned for failure scenarios");
            } else {
                Assert.assertEquals(response.getResponse().getBody().jsonPath().get("status").toString(), expectedErrorMessage, "Incorrect status message returned for failure scenario");
            }
            try {
                repository.findJournalRecord(wiproJournalsId.toString());
                verifyJournalsApiForDeletingRecord("Y", "", wiproJournalsId, request, "Success");
                Assert.assertFalse(true, "Record inserted into Database");
            } catch (NoResultException e) {
            }
        } catch (SkipException skipException) {
            log.error(skipException.getMessage());
            throw skipException;
        } catch (
                Exception e) {
            log.error("Error in test execution : ", e);
            Assert.assertTrue(false, "Exception occured in execution : " + e);
        }

    }

    @Test(priority = 1, description = "Insertion", dataProviderClass = JournalsApiDataProvider.class, dataProvider = "getJournalsApiObjectForInsertionOfRecord")
    public void verifyJournalsApiForInsertionOfRecord(String runMode, String testName, Integer wiproJournalsId, ApiRequest request, String expectedSuccessMessage) {
        try {
            if (!runMode.equalsIgnoreCase("y")) {
                throw new SkipException("Test case run mode is OFF, so skipping the test");
            }
            ModelResponse<JournalsApiResponse> response = api.updateJournalsApi(wiproJournalsId, request);
            Assert.assertEquals(response.getResponse().getBody().jsonPath().get("status").toString(), expectedSuccessMessage, "Incorrect status message returned for failure scenario");

            try {
                JnlJournalEntity jnlJournalEntity = repository.findJournalRecord(wiproJournalsId.toString());
                verifyInsertedRecord(jnlJournalEntity, (JournalsApiDto) request.getBody());

            } catch (NoResultException e) {
                Assert.assertTrue(false, "Record should insert into database");
            }

        } catch (SkipException skipException) {
            log.error(skipException.getMessage());
            throw skipException;
        } catch (Exception e) {
            log.error("Error in test execution : ", e);
            Assert.assertTrue(false, "Exception occured in execution, Failing the test" + e);
        }
    }


    private void verifyInsertedRecord(JnlJournalEntity jnlJournalEntity, JournalsApiDto expectedResult) {
        Assert.assertEquals(jnlJournalEntity.getTitle(), expectedResult.getTitle(), "Title is not correct");
        log.debug("Title verified");
        Assert.assertEquals(jnlJournalEntity.getAbbreviation(), expectedResult.getAbbreviation(), "Abbreviation is not correct");
        log.debug("Abbreviation verified");
        Assert.assertEquals(jnlJournalEntity.getIssn(), expectedResult.getIssn(), "ISSN is incorrect");
        log.debug("ISSN verified");
        Assert.assertEquals(jnlJournalEntity.getElectronicIssn(), expectedResult.getElectronicISSN(), "Electronic ISSN is incorrect");
        log.debug("Electronic ISSN verified");

        boolean journalCountryStatus = false;
        journalCountryStatus = expectedResult.getJournalCountry() != null ? (expectedResult.getJournalCountry() != "" ? true : false) : false;

        try {
            if (journalCountryStatus) {
                CMNCountryEntity cmnCountryEntity = repository.findCMNCountryRecordByCountry(expectedResult.getJournalCountry());
                Assert.assertEquals(jnlJournalEntity.getCountryId(), cmnCountryEntity.getCountryId(), "JournalCountry is incorrect");
            } else {
                try {
                    CMNCountryEntity cmnCountryEntity = repository.findCMNCountryRecordByCountry(expectedResult.getJournalCountry());
                    Assert.fail("Record should not inserted into CNMCountry table");
                } catch (NoResultException e) {
                }
            }
            log.debug("Journals Country verified");
        } catch (NoResultException e) {
            Assert.fail("JournalsCountry is not present in DB", e);
        }

        boolean languageStatus = false;
        languageStatus = expectedResult.getLanguage() != null ? (expectedResult.getLanguage() != "" ? true : false) : false;
        try {
            if (languageStatus) {
                String[] expectedLanguageArray = expectedResult.getLanguage().split(",");
                List<String> actualLanguageArray = new ArrayList<>();
                List<JnlJournalLanguageEntity> jnlJournalLanguageEntity = repository.findJournalLanguageRecordByJournalId(jnlJournalEntity.getJournalId());
                for (JnlJournalLanguageEntity record : jnlJournalLanguageEntity) {
                    JnlLanguageEntity jnlLanguageEntity = repository.findLanguageRecordByLanguageId(record.getLanguageId());
                    actualLanguageArray.add(jnlLanguageEntity.getName());
                }
                Assert.assertEquals(actualLanguageArray.size(), expectedLanguageArray.length, "Not all Language are inserted into Database");
                Assert.assertTrue(Arrays.asList(expectedLanguageArray).containsAll(actualLanguageArray), "Language List is not matching");
            } else {
                List<JnlJournalLanguageEntity> jnlJournalLanguageEntity = repository.findJournalLanguageRecordByJournalId(jnlJournalEntity.getJournalId());
                if (!jnlJournalLanguageEntity.isEmpty()) {
                    Assert.fail("Language inserted even when request does not have any language provided");
                }
            }
            log.debug("language verified");
        } catch (NoResultException e) {
            Assert.fail("Language is not present in DB", e);
        }

        boolean publisherDetailsStatus = false;
        publisherDetailsStatus = expectedResult.getPublisherDetails() != null ? (!expectedResult.getPublisherDetails().isEmpty() ? true : false) : false;

        try {
            if (publisherDetailsStatus) {
                List<PublisherDetails> actualPublisherDetails = new ArrayList<>();
                List<JnlImprintEntity> jnlImprintEntity = repository.findImprintsRecordByJournalId(jnlJournalEntity.getJournalId());
                for (JnlImprintEntity jnlImprintEntityRecord : jnlImprintEntity) {
                    //Assert.assertEquals(repository.findCMNCountryRecordByCountryId(jnlImprintEntityRecord.getCountryId()).getCountry(), "ex", "Publisher Country does not matched");
                    PublisherDetails publisherDetails = new PublisherDetails();
                    publisherDetails.setPublisherCountry(repository.findCMNCountryRecordByCountryId(jnlImprintEntityRecord.getCountryId()).getCountry());
                    publisherDetails.setPublisher(repository.findJnlOrganisationRecordByOrganisationId(jnlImprintEntityRecord.getPublisherId()).getName());
                    actualPublisherDetails.add(publisherDetails);
                }
                Assert.assertEquals(expectedResult.getPublisherDetails().size(), actualPublisherDetails.size(), "Publisher Details does not have all objects");
                Assert.assertTrue(actualPublisherDetails.containsAll(expectedResult.getPublisherDetails()), "All publisher details does not got saved");
            }else {
                List<JnlImprintEntity> jnlImprintEntity = repository.findImprintsRecordByJournalId(jnlJournalEntity.getJournalId());
                if(!jnlImprintEntity.isEmpty()){
                    Assert.fail("jnlImprints records found, should be empty");
                }
            }
            log.debug("Publisher Details verified");
        } catch (NoResultException e) {
            Assert.fail("Exception in publisher details verification", e);
        }


    }

    @Test(priority = 2, dataProviderClass = JournalsApiDataProvider.class, dataProvider = "getJournalsApiObjectForSuccessScenarios")
    public void verifyJournalsApiForSuccessScenarios(String runMode, String testName, Integer wiproJournalsId, ApiRequest request, String expectedSuccessMessage) {
        try {
            if (!runMode.equalsIgnoreCase("y")) {
                throw new SkipException("Test case run mode is OFF, so skipping the test");
            }
            ModelResponse<JournalsApiResponse> response = api.updateJournalsApi(wiproJournalsId, request);
            Assert.assertEquals(response.getResponse().getBody().jsonPath().get("status").toString(), expectedSuccessMessage, "Incorrect status message returned for failure scenario");

            try {
                JnlJournalEntity jnlJournalEntity = repository.findJournalRecord(wiproJournalsId.toString());
                verifyInsertedRecord(jnlJournalEntity, (JournalsApiDto) request.getBody());

            } catch (NoResultException e) {
                Assert.assertTrue(false, "Record should insert into database");
            }

        } catch (SkipException skipException) {
            log.error(skipException.getMessage());
            throw skipException;
        } catch (Exception e) {
            log.error("Error in test execution : ", e);
            Assert.assertTrue(false, "Exception occured in execution, Failing the test" + e);
        }
    }


    @Test(priority = 3, dataProviderClass = JournalsApiDataProvider.class, dataProvider = "getJournalsApiObjectForDeleteEndpointScenarios", dependsOnMethods = "verifyJournalsApiForInsertionOfRecord")
    public void verifyJournalsApiForDeletingRecord(String runMode, String testName, Integer wiproJournalsId, ApiRequest request, String expectedErrorMessage) {
        try {
            if (!runMode.equalsIgnoreCase("y")) {
                throw new SkipException("Test case run mode is OFF, so skipping the test");
            }
            ModelResponse<JournalsApiResponse> response = api.deleteJournalsApi(wiproJournalsId, request);
            if (response.getResponse().getBody().jsonPath().get("message") != null) {
                Assert.assertEquals(response.getResponse().getBody().jsonPath().get("message").toString(), expectedErrorMessage, "Incorrect Message returned for failure scenarios");
            } else {
                Assert.assertEquals(response.getResponse().getBody().jsonPath().get("status").toString(), expectedErrorMessage, "Incorrect status message returned for failure scenario");
            }
        } catch (SkipException skipException) {
            log.error(skipException.getMessage());
            throw skipException;
        } catch (Exception e) {
            log.error("Error in test execution : ", e);
            Assert.assertTrue(false, "Exception occured in execution, Failing the test" + e);
        }

    }


    //@Test
    public void display(ITestContext result) throws IOException, ReadExcelException {

        System.out.println(repository.findJournalRecord("0"));

//        ReadExcel journalsAPIDataProviderExcel = ReadExcel.getExcelData(Property.get("apiDataProviderExcelSheet"));
//        System.out.println(journalsAPIDataProviderExcel.getAllDataForSheet("JournalsApiDataNegativeScenario"));
    }


}
