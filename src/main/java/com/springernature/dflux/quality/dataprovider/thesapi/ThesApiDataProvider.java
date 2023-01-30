package com.springernature.dflux.quality.dataprovider.thesapi;

import com.springer.quality.utilities.Property;
import com.springer.quality.utilities.ReadExcel;
import com.springer.quality.utilities.exceptions.ReadExcelException;
import com.springernature.dflux.quality.api.dtos.request.ApiRequest;
import org.testng.annotations.DataProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ThesApiDataProvider {

    @DataProvider(name = "getThesApiRequestDataForFailureScenario")
    public Object[][] getThesApiRequestDataForFailureScenario() throws IOException, ReadExcelException {
        ReadExcel thesAPIDataProviderExcel = ReadExcel.getExcelData(Property.get("thesApiDataProviderExcelSheet"));
        return getRequestForNegativeScenarios(thesAPIDataProviderExcel.getAllDataForSheet("thesApiData"), 2, 4);
    }

    @DataProvider(name = "getThesApiRequestDataForSuccessScenario")
    public Object[][] getThesApiRequestDataForSuccessScenario() throws IOException, ReadExcelException {
        ReadExcel thesAPIDataProviderExcel = ReadExcel.getExcelData(Property.get("thesApiDataProviderExcelSheet"));
        return getRequestForSuccessScenarios(thesAPIDataProviderExcel.getAllDataForSheet("thesApiData"), 5, 35);
    }

    private Object[][] getRequestForNegativeScenarios(ArrayList<ArrayList<Object>> excelData, int startRow, int endRow) {
        Object[][] requestData = new Object[((endRow - startRow) + 1)][5];
        for (int i = startRow - 1; i <= endRow - 1; i++) {

            //setting up runMode
            requestData[i - (startRow - 1)][0] = getRunMode(excelData, i);

            //setting up TestName
            requestData[i - (startRow - 1)][1] = getTestName(excelData, i);

            //Setting up headers
            Map<String, String> headers = getHeaders(excelData, i);

            Map<String, String> queryParams = getQueryParams(excelData, i);

            ApiRequest apiRequest = new ApiRequest();
            apiRequest.setHeaders(headers);
            apiRequest.setQueryParams(queryParams);

            requestData[i - (startRow - 1)][2] = apiRequest;
            requestData[i - (startRow - 1)][3] = getStatusCode(excelData, i);
            requestData[i - (startRow - 1)][4] = getMessage(excelData, i);

        }
        return requestData;
    }

    private Object[][] getRequestForSuccessScenarios(ArrayList<ArrayList<Object>> excelData, int startRow, int endRow) {
        Object[][] requestData = new Object[((endRow - startRow) + 1)][4];
        for (int i = startRow - 1; i <= endRow - 1; i++) {

            //setting up runMode
            requestData[i - (startRow - 1)][0] = getRunMode(excelData, i);

            //setting up TestName
            requestData[i - (startRow - 1)][1] = getTestName(excelData, i);

            //Setting up headers
            Map<String, String> headers = getHeaders(excelData, i);

            Map<String, String> queryParams = getQueryParams(excelData, i);

            ApiRequest apiRequest = new ApiRequest();
            apiRequest.setHeaders(headers);
            apiRequest.setQueryParams(queryParams);

            requestData[i - (startRow - 1)][2] = apiRequest;
            requestData[i - (startRow - 1)][3] = getStatusCode(excelData, i);
        }
        return requestData;
    }

    private String getMessage(ArrayList<ArrayList<Object>> excelData, int i) {
        return excelData.get(i).get(Integer.parseInt(Property.get("columnPositionMessageThesApi"))).toString();
    }

    private int getStatusCode(ArrayList<ArrayList<Object>> excelData, int i) {
        return Double.valueOf(excelData.get(i).get(Integer.parseInt(Property.get("columnPositionResponseStatusCodeThesApi"))).toString()).intValue();
    }

    private Map<String, String> getQueryParams(ArrayList<ArrayList<Object>> excelData, int i) {
        Map<String, String> queryParams = new HashMap<>();
        if (!excelData.get(i).get(Integer.parseInt(Property.get("columnPositionLabelThesApi"))).toString().equals("null")) {
            queryParams.put("label", excelData.get(i).get(Integer.parseInt(Property.get("columnPositionLabelThesApi"))).toString());
        }
        if (!excelData.get(i).get(Integer.parseInt(Property.get("columnPositionIncludeAltLabelThesApi"))).toString().equals("null")) {
            queryParams.put("includeAltLabels", excelData.get(i).get(Integer.parseInt(Property.get("columnPositionIncludeAltLabelThesApi"))).toString());
        }
        if (!excelData.get(i).get(Integer.parseInt(Property.get("columnPositionCaseInSensitiveThesApi"))).toString().equals("null")) {
            queryParams.put("caseInsensitive", excelData.get(i).get(Integer.parseInt(Property.get("columnPositionCaseInSensitiveThesApi"))).toString());
        }
        if (!excelData.get(i).get(Integer.parseInt(Property.get("columnPositionSubStringThesApi"))).toString().equals("null")) {
            queryParams.put("substring", excelData.get(i).get(Integer.parseInt(Property.get("columnPositionSubStringThesApi"))).toString());
        }
        return queryParams;
    }

    private Map<String, String> getHeaders(ArrayList<ArrayList<Object>> excelData, int i) {
        String[] headersArray = excelData.get(i).get(Integer.parseInt(Property.get("columnPositionHeadersThesApi"))).toString().split(",");
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

    private String getTestName(ArrayList<ArrayList<Object>> excelData, int i) {
        return excelData.get(i).get(Integer.parseInt(Property.get("columnPositionTestNameThesApi"))).toString();
    }

    private String getRunMode(ArrayList<ArrayList<Object>> excelData, int i) {
        return excelData.get(i).get(Integer.parseInt(Property.get("columnPositionRunModeThesApi"))).toString();
    }
}




