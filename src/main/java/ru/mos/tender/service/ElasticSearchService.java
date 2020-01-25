package ru.mos.tender.service;

import ru.mos.tender.model.ElasticResponse;

import javax.annotation.Nonnull;

public interface ElasticSearchService {
    @Nonnull
    ElasticResponse fullTextSearch(@Nonnull String text);
}
