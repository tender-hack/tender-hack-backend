package ru.mos.tender.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.mos.tender.domain.entity.SearchEntity;
import ru.mos.tender.enums.ElasticResponseType;
import ru.mos.tender.model.ChartExtraInfo;
import ru.mos.tender.model.ElasticResponse;
import ru.mos.tender.model.ExtraInfo;
import ru.mos.tender.model.NavigationExtraInfo;
import ru.mos.tender.repository.SearchRepository;
import ru.mos.tender.service.ElasticSearchService;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Service
@ConditionalOnBean(ElasticsearchTemplate.class)
@RequiredArgsConstructor
@Slf4j
public class ElasticSearchServiceImpl implements ElasticSearchService {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ObjectMapper objectMapper;
    private final SearchRepository searchRepository;
    private final NavURIBuilder navURIBuilder;

    @PostConstruct
    @SneakyThrows({JsonProcessingException.class, IOException.class})
    public void init() {
        searchRepository.deleteAll();
        File file = new File(getClass().getClassLoader().getResource("elastic.json").getPath());
        List<SearchEntity> list = Arrays.asList(objectMapper.readValue(file, SearchEntity[].class));
        searchRepository.saveAll(list);
    }

    @Override
    public ElasticResponse fullTextSearch(String query) {
        List<SearchEntity> searchEntities = elasticsearchTemplate.queryForList(new NativeSearchQueryBuilder()
                .withQuery(matchQuery("query", query)
                        .analyzer("main_analyzer")
                        .fuzziness(Fuzziness.ONE)
                        .minimumShouldMatch("-50%"))
                .build(), SearchEntity.class);

        searchEntities.forEach(se -> se.setId(UUID.randomUUID()));

        UUID winnerID = searchEntities
                .stream().filter(ElasticResponseType::isNotNavProperty)
                .findFirst().orElse(searchEntities.get(0)).getId();

        List<SearchEntity> navs = searchEntities.stream()
                .filter(ElasticResponseType::isNav)
                .collect(Collectors.toList());

        List<ElasticResponse> elasticResponses = searchEntities
                .stream()
                .filter(ElasticResponseType::isNotNav)
                .map(this::buildElasticResponse)
                .collect(Collectors.toList());

        navURIBuilder.fromSearchEntities(navs)
                .forEach(navURL -> elasticResponses.add(new ElasticResponse()
                        .setQuery(navURL.getSearchEntity().getQuery())
                        .setSearchEntityId(navURL.getSearchEntity().getId())
                        .setExtraInfo(new NavigationExtraInfo(navURL.getUri().toString()))
                        .setType(ElasticResponseType.NAVIGATION)));

        //todo: временный костыль, надо исправить
        if (CollectionUtils.isEmpty(elasticResponses)) {
            return buildElasticResponse(SearchEntity.fallbackAnswer(query));
        }

        return elasticResponses.stream().filter(er -> er.getSearchEntityId().equals(winnerID))
                .findFirst().orElse(elasticResponses.get(0));
    }

    @Nonnull
    private ElasticResponse buildElasticResponse(@Nonnull SearchEntity entity) {
        ExtraInfo extraInfo = new ExtraInfo();
        switch (ElasticResponseType.valueOf(entity.getType().toUpperCase())) {
            case TEXT:
                extraInfo.setQuery(entity.getQuery());
                extraInfo.setText(entity.getText());
                break;
            case CHART:
                extraInfo = new ChartExtraInfo().setChartInfo(entity.getChart());
                extraInfo.setText(entity.getText());
                break;
            default:
                extraInfo = new ExtraInfo();
        }
        return new ElasticResponse()
                .setSearchEntityId(entity.getId())
                .setQuery(entity.getQuery())
                .setExtraInfo(extraInfo)
                .setType(ElasticResponseType.valueOf(entity.getType().toUpperCase()));
    }


}
