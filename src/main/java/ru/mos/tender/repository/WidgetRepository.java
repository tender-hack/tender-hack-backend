package ru.mos.tender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.mos.tender.domain.Widget;
import ru.mos.tender.enums.WidgetType;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WidgetRepository
        extends JpaRepository<Widget, Integer> {

    @Nonnull
    List<Widget> findAllByUserId(@Nonnull Long userId);

    @Modifying
    @Query("update Widget w " +
            "set w.counter = w.counter + 1 " +
            "where w.widgetUid = :widgetUid and w.userId = :userId")
    int increment(@Nonnull @Param("widgetUid") UUID widgetUid, @Nonnull @Param("userId") Long userId);

    @Query("select w from Widget as w where w.type = :type and w.extra = :extra")
    Widget findByTypeAndExtra(@Param("type") WidgetType type, @Param("extra") String extra);
}
