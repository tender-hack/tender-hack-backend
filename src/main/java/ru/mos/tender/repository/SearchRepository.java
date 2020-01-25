package ru.mos.tender.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import ru.mos.tender.model.entity.SearchEntity;

import java.util.UUID;

public interface SearchRepository extends ElasticsearchRepository<SearchEntity, UUID> {
}
