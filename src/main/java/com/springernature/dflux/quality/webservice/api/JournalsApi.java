package com.springernature.dflux.quality.webservice.api;

import com.springer.quality.api.BaseApi;
import com.springer.quality.api.item.ApiItem;
import com.springer.quality.api.model.ApiConfig;
import com.springer.quality.api.model.ApiUserConfig;
import com.springer.quality.api.response.ModelResponse;
import com.springernature.dflux.quality.api.dtos.journalsapi.JournalsApiResponse;
import com.springernature.dflux.quality.api.dtos.request.ApiRequest;
import com.springernature.dflux.quality.webservice.api.endpoints.JournalsApiEndpoints;
import com.springernature.dflux.quality.webservice.config.DfluxApiConfig;

public class JournalsApi extends BaseApi {

    public JournalsApi(ApiUserConfig user) {
        super(user);
    }

    public ModelResponse<JournalsApiResponse> updateJournalsApi(Integer wiproJournalsId, ApiRequest request) {
        return ModelResponse.wrap(init(getUser())
                .body(request.getBody()).headers(request.getHeaders())
                .put(JournalsApiEndpoints.UPDATE.getEndpoint(wiproJournalsId)), JournalsApiResponse.class);
    }

    public ModelResponse<JournalsApiResponse> deleteJournalsApi(Integer wiproJournalsId, ApiRequest request) {
        return ModelResponse.wrap(init(getUser())
                .body(request.getBody()).headers(request.getHeaders())
                .delete(JournalsApiEndpoints.DELETE.getEndpoint(wiproJournalsId)), JournalsApiResponse.class);
    }

    @Override
    protected ApiConfig getConfig() {
        return ApiItem.instance().getConfig(DfluxApiConfig.class).getJournalsApi();
    }
}
