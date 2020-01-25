package ru.mos.tender.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ChartExtraInfo extends ExtraInfo {
    private Map<String, String> chartInfo;
}
