package ru.mos.tender.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.mos.tender.enums.ElasticResponseType;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class ElasticResponse {
    @JsonIgnore
    private UUID searchEntityId;
    private String query;
    private String title;
    private ElasticResponseType type;
    private ExtraInfo extraInfo;
}
