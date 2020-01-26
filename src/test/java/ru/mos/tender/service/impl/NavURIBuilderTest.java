package ru.mos.tender.service.impl;

import org.junit.jupiter.api.Test;
import ru.mos.tender.domain.entity.SearchEntity;
import ru.mos.tender.enums.ElasticResponseType;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NavURIBuilderTest {


    @Test
    public void test_fromSearchEntities(){
        NavURIBuilder navURIBuilder = new NavURIBuilder();
        SearchEntity seURI = new SearchEntity();
        seURI.setType(ElasticResponseType.NAVIGATION.name());
        seURI.setText("xxx");

        SearchEntity seParam1 = new SearchEntity();
        seParam1.setType(ElasticResponseType.NAVIGATION_PROPERTY.name());
        seParam1.setName("s.stateIdIn");
        seParam1.setText("190001");

        SearchEntity seParam2 = new SearchEntity();
        seParam2.setType(ElasticResponseType.NAVIGATION_PROPERTY.name());
        seParam2.setName("s.stateIdIn");
        seParam2.setText("190002");
        List<NavURI> uris = navURIBuilder.fromSearchEntities(Arrays.asList(
                seURI, seParam1, seParam2
        ));
        assertEquals(1, uris.size());
        for (NavURI navURI : uris) {
            assertEquals("xxx?s.stateIdIn.0=190001&s.stateIdIn.1=190001", navURI.getUri().toString());
        }
    }
}