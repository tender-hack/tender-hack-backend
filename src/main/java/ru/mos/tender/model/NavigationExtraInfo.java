package ru.mos.tender.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class NavigationExtraInfo extends ExtraInfo {
    private String url;

    public static NavigationExtraInfo valueOf(String url, String query, String text) {
        NavigationExtraInfo i = new NavigationExtraInfo();
        i.url = url;
        i.setQuery(query);
        i.setText(text);
        return i;
    }
}
