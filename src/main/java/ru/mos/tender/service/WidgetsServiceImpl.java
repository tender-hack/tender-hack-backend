package ru.mos.tender.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mos.tender.domain.Widget;
import ru.mos.tender.model.WidgetInfo;
import ru.mos.tender.repository.WidgetRepository;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class WidgetsServiceImpl
        implements WidgetsService {

    private final WidgetRepository widgetRepository;

    @Nonnull
    @Override
    public List<WidgetInfo> getUserWidgets(@Nonnull Long userId) {
        return widgetRepository
                .findAllByUserId(userId)
                .stream()
                .sorted(comparingInt(Widget::getCounter))
                .map(this::toWidgetInfo)
                .collect(toList());
    }

    @Override
    public void increment(@Nonnull UUID widgetUid, @Nonnull Long userId) {
        widgetRepository.increment(widgetUid, userId);
    }

    @Nonnull
    private WidgetInfo toWidgetInfo(@Nonnull Widget widget) {
        return new WidgetInfo()
                .setUid(widget.getWidgetUid())
                .setType(widget.getType());
    }
}
