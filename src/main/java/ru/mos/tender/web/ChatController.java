package ru.mos.tender.web;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.*;
import ru.mos.tender.model.Answer;
import ru.mos.tender.model.ElasticResponse;
import ru.mos.tender.model.Question;
import ru.mos.tender.service.ChatService;
import ru.mos.tender.service.ElasticSearchService;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
@ConditionalOnBean(ElasticsearchTemplate.class)
public class ChatController {
    private static final long USER_ID = 1;

    private final ChatService chatService;
    private final ElasticSearchService elasticSearchService;

    @PostMapping(
            path = "/default",
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    public Answer processQuestion(Question question) {
        return chatService.process(question);
    }

    @GetMapping(
            path = "/answer",
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    public ElasticResponse getResponse(String text){
        return elasticSearchService.fullTextSearch(text); //todo: test endpoint for elastic
    }

}
