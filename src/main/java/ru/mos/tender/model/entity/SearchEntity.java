package ru.mos.tender.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.Setting;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.UUID;

import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

@Document(indexName = "search")
@Setting(settingPath = "settings/setting.json")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Field(type = Text, fielddata = true, analyzer = "main_analyzer")
    private String query;

    private String text;

    private String type;

}
