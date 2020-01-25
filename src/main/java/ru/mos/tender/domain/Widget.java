package ru.mos.tender.domain;

import com.google.common.base.MoreObjects;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import ru.mos.tender.enums.WidgetType;

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
public class Widget {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "widget_uid", nullable = false)
    private UUID widgetUid;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private WidgetType type;

    @Column(name = "counter")
    private Integer counter;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Widget widget = (Widget) o;
        return new EqualsBuilder()
                .append(userId, widget.userId)
                .append(widgetUid, widget.widgetUid)
                .append(type, widget.type)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(userId)
                .append(widgetUid)
                .append(type)
                .toHashCode();
    }

    @Override
    public String toString() {
        return MoreObjects
                .toStringHelper(this)
                .add("userId", userId)
                .add("widgetUid", widgetUid)
                .add("type", type)
                .toString();
    }
}
