package ru.mos.tender.enums;

import io.netty.util.internal.StringUtil;
import org.thymeleaf.util.StringUtils;
import ru.mos.tender.domain.entity.SearchEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public enum ElasticResponseType {
    NAVIGATION, NAVIGATION_PROPERTY, TEXT, CHART;

    public static List<ElasticResponseType> NAVS = Arrays.asList(NAVIGATION, NAVIGATION_PROPERTY);

    public static boolean isNotNav(SearchEntity searchEntity) {
        return !isNav(searchEntity);
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
