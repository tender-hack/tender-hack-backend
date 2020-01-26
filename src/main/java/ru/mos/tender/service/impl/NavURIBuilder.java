package ru.mos.tender.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.util.CollectionUtils;
import ru.mos.tender.domain.entity.SearchEntity;
import ru.mos.tender.enums.ElasticResponseType;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Slf4j
public class NavURIBuilder {

    public List<URI> fromSearchEntities(Collection<SearchEntity> searchEntities) {
        List<URI> results = new ArrayList<>();
        try {
            List<SearchEntity> uriList = new ArrayList<>();
            List<SearchEntity> uriParams = new ArrayList<>();

            for (SearchEntity searchEntity : searchEntities) {
                ElasticResponseType elasticResponseType = ElasticResponseType.byName(searchEntity.getType());
                if (elasticResponseType == null) {
                    log.warn("Can not parse ElasticResponseType: {}", searchEntity.getType());
                    continue;
                }
                switch (elasticResponseType) {
                    case NAVIGATION:
                        uriList.add(searchEntity);
                        break;
                    case NAVIGATION_PROPERTY:
                        uriParams.add(searchEntity);
                        break;
                }
            }
            if (CollectionUtils.isEmpty(uriList)) {
                log.warn("no uris entities found");
            }
            for (SearchEntity uriSE : uriList) {
                URIBuilder builder = new URIBuilder(uriSE.getUrl());
                Map<String, String> params = parseParameters(uriParams);
                for (Map.Entry<String, String> p : params.entrySet()) {
                    builder.addParameter(p.getKey(), p.getValue());
                }
                results.add(builder.build());
            }
        } catch (URISyntaxException e) {
            log.error(e.getMessage(), e);
            return null;
        }

        return results;
    }

    private Map<String, String> parseParameters(List<SearchEntity> uriParams) {
        Map<String, List<String>> parametersMap = new HashMap<>();
        for (SearchEntity uriParam : uriParams) {
            List<String> params = parametersMap.get(uriParam.getName());
            if (params == null) {
                List<String> value = new ArrayList<>();
                value.add(uriParam.getText());
                parametersMap.put(uriParam.getName(), value);
                continue;
            }
            params.add(uriParam.getText());
        }

        Map<String, String> fixedParametersMap = new HashMap<>();
        for (Map.Entry<String, List<String>> pmEntity : parametersMap.entrySet()) {
            List<String> paramsList = pmEntity.getValue();
            if (paramsList.size() == 1) {
                fixedParametersMap.put(pmEntity.getKey(), paramsList.get(0));
                continue;
            }
            for (int i = 0; i < paramsList.size(); i++) {
                fixedParametersMap.put(String.format("%s.%d", pmEntity.getKey(), i), paramsList.get(0));
            }
        }
        return fixedParametersMap;
    }

}
