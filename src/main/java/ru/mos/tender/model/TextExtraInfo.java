package ru.mos.tender.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class TextExtraInfo extends ExtraInfo {
    private String text;
}
