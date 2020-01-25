package ru.mos.tender.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mos.tender.domain.Widget;
import ru.mos.tender.enums.WidgetType;
import ru.mos.tender.model.ExtraInfo;
import ru.mos.tender.model.NavigationExtraInfo;
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
    private final Gson gson = new GsonBuilder().create();

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
    @Override
    public WidgetInfo saveNewWidget(@Nonnull Widget widget) {
        widget.setCounter(0);
        widgetRepository.save(widget);
        return toWidgetInfo(widget);
    }

    @Nonnull
    private WidgetInfo toWidgetInfo(@Nonnull Widget widget) {
        ExtraInfo extraInfo = gson.fromJson(widget.getExtra(), getExtraInfoClass(widget.getType()));
        return new WidgetInfo()
                .setUid(widget.getWidgetUid())
                .setType(widget.getType())
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
