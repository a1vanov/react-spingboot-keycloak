package com.innrate.common.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.innrate.common.database.model.Base;

import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import java.time.Clock;

@Component
public class BaseListener {

    private final Clock clock;

    @Autowired
    public BaseListener(Clock clock) {
        this.clock = clock;
    }

    @PrePersist
    public <T extends Base> void prePersist(T base) {
        base.setCreatedDate(clock.instant());
    }

    @PostUpdate
    public void postUpdate(Base base) {
        base.setUpdatedDate(clock.instant());
    }
}
