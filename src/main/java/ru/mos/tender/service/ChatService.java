package ru.mos.tender.service;

import ru.mos.tender.model.Answer;
import ru.mos.tender.model.Question;

import javax.annotation.Nonnull;

public interface ChatService {

    @Nonnull
    Answer process(Question question);

}
