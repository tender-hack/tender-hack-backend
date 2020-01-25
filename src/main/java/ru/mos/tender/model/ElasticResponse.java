package ru.mos.tender.model;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.mos.tender.enums.ElasticResponseType;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class ElasticResponse {
    private String query;
    private ElasticResponseType type;
    private ExtraInfo extraInfo;
}
