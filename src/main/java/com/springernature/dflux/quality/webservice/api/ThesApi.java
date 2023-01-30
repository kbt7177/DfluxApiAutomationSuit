package com.springernature.dflux.quality.webservice.api;

import com.springer.quality.api.BaseApi;
import com.springer.quality.api.item.ApiItem;
import com.springer.quality.api.model.ApiConfig;
import com.springer.quality.api.model.ApiUserConfig;
import com.springer.quality.api.response.ListResponse;
import com.springernature.dflux.quality.api.dtos.request.ApiRequest;
import com.springernature.dflux.quality.api.dtos.thesapi.ThesApiResponse;
import com.springernature.dflux.quality.webservice.api.endpoints.JournalsApiEndpoints;
import com.springernature.dflux.quality.webservice.api.endpoints.ThesApiEndpoints;
import com.springernature.dflux.quality.webservice.config.DfluxApiConfig;

import java.util.HashMap;
import java.util.Map;

public class ThesApi extends BaseApi {

    public ThesApi(ApiUserConfig user) {
        super(user);
    }

    public ListResponse<ThesApiResponse> getThesTerm(ApiRequest request) {
        return ListResponse.wrap(init(getUser())
                .queryParams(request.getQueryParams()).headers(request.getHeaders())
                .get(ThesApiEndpoints.TERM_SEARCH.getEndpoint()), ThesApiResponse.class);
    }

    @Override
    protected ApiConfig getConfig() {
        return ApiItem.instance().getConfig(DfluxApiConfig.class).getThesApi();
    }
}
