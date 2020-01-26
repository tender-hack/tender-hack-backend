package ru.mos.tender.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import ru.mos.tender.domain.Widget;
import ru.mos.tender.enums.ElasticResponseType;
import ru.mos.tender.model.ElasticResponse;
import ru.mos.tender.model.ExtraInfo;
import ru.mos.tender.model.Question;
import ru.mos.tender.service.ChatService;
import ru.mos.tender.service.ElasticSearchService;
import ru.mos.tender.service.WidgetsService;

import javax.annotation.Nonnull;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnBean(ElasticsearchTemplate.class)
public class ChatServiceImpl implements ChatService {

    private final ElasticSearchService elasticSearchService;
    private final WidgetsService widgetsService;
    private final ExecutorService widgetCreatingExecutorService;

    @Nonnull
    @Override
    public ExtraInfo process(Question question) {
        log.info(question.toString());
        ElasticResponse response = elasticSearchService.fullTextSearch(question.getText());
        if (response.getType().equals(ElasticResponseType.NAVIGATION)) {
            widgetCreatingExecutorService.submit(() -> {
                        try {
                            widgetsService.createIfNotExists(Widget.fromNavigationAnswer(1L, response));
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    }
            );
        }
        ExtraInfo answer = response.getExtraInfo();
        log.info(answer.toString());
        return answer;
    }
}
