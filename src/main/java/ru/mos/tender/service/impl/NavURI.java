package ru.mos.tender.service.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mos.tender.domain.entity.SearchEntity;

import java.net.URI;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NavURI {
    private SearchEntity searchEntity;
    private URI uri;
}
