package ru.mos.tender.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.Setting;
import ru.mos.tender.model.ChartItem;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;
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

    private String name;

    private String type;

    private String url;

    private List<ChartItem> chart;

    public static SearchEntity fallbackAnswer(String query) {
        SearchEntity searchEntity = new SearchEntity();
        searchEntity.setQuery(query);
        searchEntity.setText("Извините, я вас не поняла!");
        searchEntity.setType("TEXT");
        return searchEntity;
    }

}
