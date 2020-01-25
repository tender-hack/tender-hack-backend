package ru.mos.tender.repository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import ru.mos.tender.model.entity.SearchEntity;

import java.util.UUID;

@ConditionalOnBean(ElasticsearchTemplate.class)
public interface SearchRepository extends ElasticsearchRepository<SearchEntity, UUID> {
}
