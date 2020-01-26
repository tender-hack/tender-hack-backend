package ru.mos.tender.model;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.mos.tender.enums.WidgetSubType;
import ru.mos.tender.enums.WidgetType;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class WidgetInfo {
    private UUID uid;
    private String name;
    private WidgetType type;
    private WidgetSubType subType;
    private ExtraInfo extraInfo;
}
