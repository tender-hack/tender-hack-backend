package ru.mos.tender.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import ru.mos.tender.enums.WidgetType;
import ru.mos.tender.model.NavigationExtraInfo;
import ru.mos.tender.model.TextExtraInfo;
import ru.mos.tender.model.WidgetInfo;
import ru.mos.tender.model.entity.SearchEntity;
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
    public WidgetInfo fullTextSearch(String query) {
        WidgetInfo widgetInfo = elasticsearchTemplate.queryForList(new NativeSearchQueryBuilder()
                .withQuery(matchQuery("query", query)
                        .analyzer("main_analyzer")
                        .fuzziness(Fuzziness.ONE)
                        .minimumShouldMatch("-50%"))
                .build(), SearchEntity.class)
                .stream()
                .map(this::buildWidgetInfo)
                .collect(Collectors.toList())
                .get(0); //todo: временный костыль, надо исправить
        return widgetInfo;
    }

    @Nonnull
    private WidgetInfo buildWidgetInfo(@Nonnull SearchEntity entity) {
        return new WidgetInfo()
                .setUid(UUID.randomUUID())
                .setName(StringUtils.EMPTY)
                .setExtraInfo(
                        WidgetType.valueOf(entity.getType().toUpperCase()).equals(WidgetType.TEXT) ?
                                new TextExtraInfo()
                                        .setText(entity.getText()) :
                                new NavigationExtraInfo()
                                        .setUrl(entity.getText())
                )
                .setType(WidgetType.valueOf(entity.getType().toUpperCase()));
    }


}
