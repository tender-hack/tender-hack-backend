package ru.mos.tender.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mos.tender.domain.Widget;
import ru.mos.tender.enums.WidgetType;
import ru.mos.tender.model.ExtraInfo;
import ru.mos.tender.model.NavigationExtraInfo;
import ru.mos.tender.model.WidgetInfo;
import ru.mos.tender.repository.WidgetRepository;
import ru.mos.tender.service.WidgetsService;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class WidgetsServiceImpl implements WidgetsService {
    private static final Long DEFAULT_USER_ID = 0L;

    private final WidgetRepository widgetRepository;
    private final Gson gson = new GsonBuilder().create();

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<WidgetInfo> getDefaultWidgets() {
        return widgetRepository
                .findAllByUserId(DEFAULT_USER_ID)
                .stream()
                .sorted((w1, w2) -> w2.getCounter() - w1.getCounter())
                .map(this::toWidgetInfo)
                .collect(toList());
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<WidgetInfo> getUserWidgets(@Nonnull Long userId) {
        return widgetRepository
                .findAllByUserId(userId)
                .stream()
                .sorted((w1, w2) -> w2.getCounter() - w1.getCounter())
                .map(this::toWidgetInfo)
                .collect(toList());
    }

    @Override
    @Transactional
    public void increment(@Nonnull UUID widgetUid, @Nonnull Long userId) {
        widgetRepository.increment(widgetUid, userId);
    }

    @Nonnull
    @Override
    @Transactional
    public WidgetInfo create(@Nonnull Widget widget) {
        widget
                .setCounter(2)
                .setUserId(1L)
                .setWidgetUid(UUID.randomUUID());

        widget = widgetRepository.save(widget);
        return toWidgetInfo(widget);
    }

    @Override
    @Transactional
    public void createIfNotExists(Widget widgetToPersist) {
        switch (widgetToPersist.getType()) {
            case NAVIGATION:
                Widget persistedEntity = widgetRepository.findByTypeAndExtra(widgetToPersist.getType(), widgetToPersist.getExtra());
                if (persistedEntity == null) {
                    log.info("navigation widget will be created: {}", widgetToPersist.toString());
                     create(widgetToPersist);
                     return;
                }
                break;
            default:
                create(widgetToPersist);
        }
    }

    @Nonnull
    private WidgetInfo toWidgetInfo(@Nonnull Widget widget) {
        ExtraInfo extraInfo = gson.fromJson(widget.getExtra(), getExtraInfoClass(widget.getType()));
        return new WidgetInfo()
                .setUid(widget.getWidgetUid())
                .setName(widget.getName())
                .setType(widget.getType())
                .setSubType(widget.getSubType())
                .setExtraInfo(extraInfo);
    }

    @Nonnull
    private Class<? extends ExtraInfo> getExtraInfoClass(@Nonnull WidgetType type) {
        switch (type) {
            case NAVIGATION:
                return NavigationExtraInfo.class;
            default:
                throw new RuntimeException("Unknown widget type");
        }
    }
}
