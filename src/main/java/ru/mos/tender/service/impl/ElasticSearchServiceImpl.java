package ru.mos.tender.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import ru.mos.tender.domain.entity.SearchEntity;
import ru.mos.tender.enums.ElasticResponseType;
import ru.mos.tender.model.ElasticResponse;
import ru.mos.tender.model.ExtraInfo;
import ru.mos.tender.model.NavigationExtraInfo;
import ru.mos.tender.model.TextExtraInfo;
import ru.mos.tender.repository.SearchRepository;
import ru.mos.tender.service.ElasticSearchService;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Service
@ConditionalOnBean(ElasticsearchTemplate.class)
public class ElasticSearchServiceImpl implements ElasticSearchService {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ObjectMapper objectMapper;
    private final SearchRepository searchRepository;

    public ElasticSearchServiceImpl(ElasticsearchTemplate elasticsearchTemplate,
                                    ObjectMapper objectMapper,
                                    SearchRepository searchRepository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.objectMapper = objectMapper;
        this.searchRepository = searchRepository;
    }

    @PostConstruct
    @SneakyThrows({JsonProcessingException.class, IOException.class})
    public void init() {
        File file = new File(getClass().getClassLoader().getResource("elastic.json").getPath());
        List<SearchEntity> list = Arrays.asList(objectMapper.readValue(file, SearchEntity[].class));
        searchRepository.saveAll(list);
    }

    @Override
    public ElasticResponse fullTextSearch(String query) {
        List<ElasticResponse> elasticResponse = elasticsearchTemplate.queryForList(new NativeSearchQueryBuilder()
                .withQuery(matchQuery("query", query)
                        .analyzer("main_analyzer")
                        .fuzziness(Fuzziness.ONE)
                        .minimumShouldMatch("-50%"))
                .build(), SearchEntity.class)
                .stream()
                .map(this::buildElasticResponse)
                .collect(Collectors.toList());
        //todo: временный костыль, надо исправить
        return elasticResponse.isEmpty() ? null : elasticResponse.get(0);
    }

    @Nonnull
    private ElasticResponse buildElasticResponse(@Nonnull SearchEntity entity) {
        ExtraInfo extraInfo;
        switch (ElasticResponseType.valueOf(entity.getType().toUpperCase())) {
            case TEXT:
                extraInfo = new TextExtraInfo().setText(entity.getText());
                break;
            case NAVIGATION:
                extraInfo = new NavigationExtraInfo().setUrl(entity.getText());
                break;
            default:
                extraInfo = new ExtraInfo();
        }
        return new ElasticResponse()
                .setQuery(entity.getQuery())
                .setExtraInfo(extraInfo)
                .setType(ElasticResponseType.valueOf(entity.getType().toUpperCase()));
    }


}
