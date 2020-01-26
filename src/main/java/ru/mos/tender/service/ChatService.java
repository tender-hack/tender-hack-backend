package ru.mos.tender.service;

import ru.mos.tender.model.ExtraInfo;
import ru.mos.tender.model.Question;

import javax.annotation.Nonnull;

public interface ChatService {

    @Nonnull
    ExtraInfo process(Question question);

}
