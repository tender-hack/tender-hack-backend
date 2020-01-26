package ru.mos.tender.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class ChartItem {
    private String name;
    private String percent;
    private String legend;

}
