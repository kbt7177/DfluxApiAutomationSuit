package com.springernature.dflux.quality.dataprovider.journalsapi;


import com.springer.quality.utilities.Property;
import com.springer.quality.utilities.ReadExcel;
import com.springer.quality.utilities.exceptions.ReadExcelException;
import com.springernature.dflux.quality.api.dtos.journalsapi.JournalsApiDto;
import com.springernature.dflux.quality.api.dtos.journalsapi.PublisherDetails;
import com.springernature.dflux.quality.api.dtos.request.ApiRequest;
import org.testng.annotations.DataProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JournalsApiDataProvider {

    @DataProvider(name = "getJournalsApiObjectForFailScenarios")
    public Object[][] getJournalsApiObjectForFailScenarios() throws IOException, ReadExcelException {
        ReadExcel journalsAPIDataProviderExcel = ReadExcel.getExcelData(Property.get("journalsApiDataProviderExcelSheet"));
        return getRequest(journalsAPIDataProviderExcel.getAllDataForSheet("JournalsApiDataNegativeScenario"), 2, 15);
    }

    @DataProvider(name = "getJournalsApiObjectForInsertionOfRecord")
    public Object[][] getJournalsApiObjectForInsertionOfRecord() throws IOException, ReadExcelException {
        ReadExcel journalsAPIDataProviderExcel = ReadExcel.getExcelData(Property.get("journalsApiDataProviderExcelSheet"));
        return getRequest(journalsAPIDataProviderExcel.getAllDataForSheet("JournalsApiDataSuccessScenario"), 2, 2);
    }

    @DataProvider(name = "getJournalsApiObjectForSuccessScenarios")
    public Object[][] getJournalsApiObjectForSuccessScenarios() throws IOException, ReadExcelException {
        ReadExcel journalsAPIDataProviderExcel = ReadExcel.getExcelData(Property.get("journalsApiDataProviderExcelSheet"));
        return getRequest(journalsAPIDataProviderExcel.getAllDataForSheet("JournalsApiDataSuccessScenario"), 3, 11);
    }


    @DataProvider(name = "getJournalsApiObjectForDeleteEndpointScenarios")
    public Object[][] getJournalsApiObjectForDeleteEndpointScenarios() throws IOException, ReadExcelException {
        ReadExcel journalsAPIDataProviderExcel = ReadExcel.getExcelData(Property.get("journalsApiDataProviderExcelSheet"));
        return getRequest(journalsAPIDataProviderExcel.getAllDataForSheet("JnlApiDeleteEndpointScenario"), 2, 5);
    }


    private static Object[][] getRequest(ArrayList<ArrayList<Object>> excelData, int startRow, int endRow) {
        Object[][] requestData = new Object[((endRow - startRow) + 1)][5];
        for (int i = startRow - 1; i <= endRow - 1; i++) {

            //setting up runMode
            requestData[i - (startRow - 1)][0] = getRunMode(excelData, i);

            //setting up TestName
            requestData[i - (startRow - 1)][1] = getTestName(excelData, i);

            //Setting up wipro journal ID
            requestData[i - (startRow - 1)][2] = getWiproJournalsId(excelData, i);

            //Setting up headers
            Map<String, String> headers = getHeaders(excelData, i);

            //Setting up apiRequestBody
            JournalsApiDto dto = getJournalsApiDto(excelData, i);


            //Setting up publisher details
            List<PublisherDetails> publisherDetailsList = getPublisherDetails(excelData, i);
            dto.setPublisherDetails(publisherDetailsList);

            ApiRequest<JournalsApiDto> apiRequest = new ApiRequest<>();
            apiRequest.setHeaders(headers);
            apiRequest.setBody(dto);

            requestData[i - (startRow - 1)][3] = apiRequest;
            requestData[i - (startRow - 1)][4] = getErrorMessage(excelData, i);
        }

        return requestData;
    }

    private static String getErrorMessage(ArrayList<ArrayList<Object>> excelData, int i) {
        return excelData.get(i).get(Integer.parseInt(Property.get("columnPositionExpectedErrorMessageJournalsApi"))).toString();
    }

    private static List<PublisherDetails> getPublisherDetails(ArrayList<ArrayList<Object>> excelData, int i) {
        String[] publisherDetailsArray = excelData.get(i).get(Integer.parseInt(Property.get("columnPositionPublisherDetailsJournalsApi"))).toString().split(",");
        int k = 0;
        List<PublisherDetails> publisherDetailsList = new ArrayList<>();
        while (k < publisherDetailsArray.length - 1) {
            PublisherDetails publisherDetails = new PublisherDetails();
            boolean status = false;
            if (!publisherDetailsArray[k].trim().equalsIgnoreCase("null")) {
                publisherDetails.setPublisherCountry(publisherDetailsArray[k].trim());
                status = true;
            }
            k = k + 1;
            if (!publisherDetailsArray[k].trim().equalsIgnoreCase("null")) {
                publisherDetails.setPublisher(publisherDetailsArray[k].trim());
                status = true;
            }
            k = k + 1;
            if (status) {
                publisherDetailsList.add(publisherDetails);
            }
        }
        return publisherDetailsList;
    }

    private static JournalsApiDto getJournalsApiDto(ArrayList<ArrayList<Object>> excelData, int i) {
        JournalsApiDto dto = new JournalsApiDto();
        if (!excelData.get(i).get(Integer.parseInt(Property.get("columnPositionTitleJournalsApi"))).toString().equals("null")) {
            dto.setTitle(excelData.get(i).get(Integer.parseInt(Property.get("columnPositionTitleJournalsApi"))).toString());
        }
        if (!excelData.get(i).get(Integer.parseInt(Property.get("columnPositionAbbreviationJournalsApi"))).toString().equals("null")) {
            dto.setAbbreviation(excelData.get(i).get(Integer.parseInt(Property.get("columnPositionAbbreviationJournalsApi"))).toString());
        }

        if (!excelData.get(i).get(Integer.parseInt(Property.get("columnPositionISSNJournalsApi"))).toString().equals("null")) {
            dto.setIssn(excelData.get(i).get(Integer.parseInt(Property.get("columnPositionISSNJournalsApi"))).toString());
        }

        if (!excelData.get(i).get(Integer.parseInt(Property.get("columnPositionElectronicISSNJournalsApi"))).toString().equals("null")) {
            dto.setElectronicISSN(excelData.get(i).get(Integer.parseInt(Property.get("columnPositionElectronicISSNJournalsApi"))).toString());
        }

        if (!excelData.get(i).get(Integer.parseInt(Property.get("columnPositionJournalCountryJournalsApi"))).toString().equals("null")) {
            dto.setJournalCountry(excelData.get(i).get(Integer.parseInt(Property.get("columnPositionJournalCountryJournalsApi"))).toString());
        }

        if (!excelData.get(i).get(Integer.parseInt(Property.get("columnPositionLanguageJournalsApi"))).toString().equals("null")) {
            dto.setLanguage(excelData.get(i).get(Integer.parseInt(Property.get("columnPositionLanguageJournalsApi"))).toString());
        }

        if (!excelData.get(i).get(Integer.parseInt(Property.get("columnPositionJournalUrlJournalsApi"))).toString().equals("null")) {
            dto.setJournalUrl(excelData.get(i).get(Integer.parseInt(Property.get("columnPositionJournalUrlJournalsApi"))).toString());
        }
        return dto;
    }

    private static Map<String, String> getHeaders(ArrayList<ArrayList<Object>> excelData, int i) {
        String[] headersArray = excelData.get(i).get(Integer.parseInt(Property.get("columnPositionHeadersJournalsApi"))).toString().split(",");
        Map<String, String> headers = new HashMap<>();
        for (String pair : headersArray) {
            if (!(pair.isEmpty() || pair.equals(""))) {
                String[] entry = pair.split("=");
                String value = null;
                if (pair.contains("=")) {
                    value = entry[1].trim();
                }
                headers.put(entry[0].trim(), value);
            }
        }
        return headers;
    }

    private static int getWiproJournalsId(ArrayList<ArrayList<Object>> excelData, int i) {
        return Double.valueOf(excelData.get(i).get(Integer.parseInt(Property.get("columnPositionWiproJournalsIdJournalsApi"))).toString()).intValue();
    }

    private static String getTestName(ArrayList<ArrayList<Object>> excelData, int i) {
        return excelData.get(i).get(Integer.parseInt(Property.get("columnPositionTestNameJournalsApi"))).toString();
    }

    private static String getRunMode(ArrayList<ArrayList<Object>> excelData, int i) {
        return excelData.get(i).get(Integer.parseInt(Property.get("columnPositionRunModeJournalsApi"))).toString();
    }
}
