package ru.mos.tender.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import ru.mos.tender.model.Answer;
import ru.mos.tender.model.ElasticResponse;
import ru.mos.tender.model.Question;
import ru.mos.tender.service.ChatService;
import ru.mos.tender.service.ElasticSearchService;

import javax.annotation.Nonnull;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnBean(ElasticsearchTemplate.class)
public class ChatServiceImpl
        implements ChatService {
    private static final Long DEFAULT_USER_ID = 0L;

    private final Gson gson = new GsonBuilder().create();
    private final ElasticSearchService elasticSearchService;

    @Nonnull
    @Override
    public Answer process(Question question) {
        ElasticResponse response = elasticSearchService.fullTextSearch(question.getText());
        Answer answer = new Answer();
        answer.setAnswerField(response.getExtraInfo());
        return answer;
    }

}
