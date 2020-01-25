package ru.mos.tender.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mos.tender.model.WidgetInfo;
import ru.mos.tender.service.WidgetsService;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/widgets")
public class WidgetsController {
    private static final long USER_ID = 1;

    private final WidgetsService widgetsService;

    @GetMapping(
            path = "/default",
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    public List<WidgetInfo> getDefaultWidgets() {
        return widgetsService.getDefaultWidgets();
    }

    @GetMapping(
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    public List<WidgetInfo> getUserWidgets() {
        return widgetsService.getUserWidgets(USER_ID);
    }

    @PostMapping(
            path = "/{uid}/increment",
            consumes = APPLICATION_JSON_UTF8_VALUE,
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    public void increment(@PathVariable("uid") UUID widgetUid) {
        widgetsService.increment(widgetUid, USER_ID);
    }
}
