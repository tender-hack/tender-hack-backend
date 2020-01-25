package ru.mos.tender.service;

import ru.mos.tender.model.WidgetInfo;

import javax.annotation.Nonnull;

public interface ElasticSearchService {
    @Nonnull
    WidgetInfo fullTextSearch(@Nonnull String text);
}
