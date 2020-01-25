package ru.mos.tender.service;

import ru.mos.tender.model.WidgetInfo;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public interface WidgetsService {

    @Nonnull
    List<WidgetInfo> getUserWidgets(@Nonnull Long userId);

    void increment(@Nonnull UUID widgetUid, @Nonnull Long userId);
}
