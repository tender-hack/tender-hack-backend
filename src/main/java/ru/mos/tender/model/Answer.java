package ru.mos.tender.model;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.mos.tender.enums.WidgetType;

import java.util.Map;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class Answer {
    private ExtraInfo answerField;
}
