package ru.mos.tender.web;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mos.tender.model.ExtraInfo;
import ru.mos.tender.model.Question;
import ru.mos.tender.service.ChatService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
@ConditionalOnBean(ElasticsearchTemplate.class)
public class ChatController {
    private static final long USER_ID = 1;

    private final ChatService chatService;

    @PostMapping("/default")
    public ExtraInfo processQuestion(@RequestBody Question question) {
        return chatService.process(question);
    }


}
