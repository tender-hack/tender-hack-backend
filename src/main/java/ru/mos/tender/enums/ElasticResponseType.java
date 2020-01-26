package ru.mos.tender.enums;

import org.thymeleaf.util.StringUtils;
import ru.mos.tender.domain.entity.SearchEntity;

import java.util.Arrays;
import java.util.List;

public enum ElasticResponseType {
    NAVIGATION, NAVIGATION_PROPERTY, TEXT, CHART;

    public static List<ElasticResponseType> NAVS = Arrays.asList(NAVIGATION, NAVIGATION_PROPERTY);

    public static boolean isNotNav(SearchEntity searchEntity) {
        return !isNav(searchEntity);
    }

    public static boolean isNotNavProperty(SearchEntity searchEntity) {
        return !isNavProperty(searchEntity);
    }

    private static boolean isNavProperty(SearchEntity searchEntity) {
        return StringUtils.equalsIgnoreCase(NAVIGATION_PROPERTY.name(), searchEntity.getType());
    }

    public static boolean isNav(SearchEntity searchEntity) {
        for (ElasticResponseType nav : NAVS) {
            if (StringUtils.equalsIgnoreCase(nav.name(), searchEntity.getType())) {
                return true;
            }
        }

        return false;
    }

    public static ElasticResponseType byName(String name) {
        for (ElasticResponseType nav : NAVS) {
            if (StringUtils.equalsIgnoreCase(nav.name(), name)) {
                return nav;
            }
        }

        return null;
    }
}
