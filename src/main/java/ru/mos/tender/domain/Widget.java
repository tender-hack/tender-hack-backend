package ru.mos.tender.domain;

import com.google.common.base.MoreObjects;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import ru.mos.tender.enums.WidgetType;
import ru.mos.tender.model.ElasticResponse;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table(name = "widgets")
@Accessors(chain = true)
public class Widget {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "widget_uid", nullable = false)
    private UUID widgetUid;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private WidgetType type;

    @Column(name = "sub_type")
    private String subType;

    @Column(name = "counter")
    private Integer counter;

    @Column(name = "extra", columnDefinition="text")
    private String extra;

    public static Widget fromNavigationAnswer(Long userId, ElasticResponse response) {
        Gson gson = new GsonBuilder().create();
        Widget widget = new Widget();
        widget.setCounter(1);
        widget.setExtra(gson.toJson(response.getExtraInfo()));
        widget.setName(response.getQuery());
        widget.setType(WidgetType.NAVIGATION);
        widget.setUserId(userId);
        return widget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Widget widget = (Widget) o;
        return new EqualsBuilder()
                .append(userId, widget.userId)
                .append(widgetUid, widget.widgetUid)
                .append(name, widget.name)
                .append(type, widget.type)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(userId)
                .append(widgetUid)
                .append(name)
                .append(type)
                .toHashCode();
    }

    @Override
    public String toString() {
        return MoreObjects
                .toStringHelper(this)
                .add("userId", userId)
                .add("widgetUid", widgetUid)
                .add("name", name)
                .add("type", type)
                .toString();
    }
}
