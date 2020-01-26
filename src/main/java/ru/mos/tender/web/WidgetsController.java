package ru.mos.tender.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.mos.tender.domain.Widget;
import ru.mos.tender.enums.WidgetType;
import ru.mos.tender.model.WidgetInfo;
import ru.mos.tender.service.WidgetsService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/widgets")
public class WidgetsController {
    private static final long USER_ID = 1;

    private final WidgetsService widgetsService;

    @GetMapping("/default")
    public List<WidgetInfo> getDefaultWidgets() {
        return widgetsService.getDefaultWidgets();
    }

    @GetMapping
    public List<WidgetInfo> getUserWidgets() {
        return widgetsService.getUserWidgets(USER_ID);
    }

    @PostMapping(path = "/{uid}/increment")
    public void increment(@PathVariable("uid") UUID widgetUid) {
        widgetsService.increment(widgetUid, USER_ID);
    }

    @GetMapping(path = "/save")
    public void saveNewWidget() {
        Widget widget = new Widget()
                .setExtra("{\"url\":\"/contracts\"}")
                .setName("Перейти в мои контракты")
                .setType(WidgetType.NAVIGATION)
                .setSubType("CONTRACT");
        widgetsService.create(widget);
    }
}
