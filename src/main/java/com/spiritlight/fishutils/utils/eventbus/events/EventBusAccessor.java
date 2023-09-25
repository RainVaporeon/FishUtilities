package com.spiritlight.fishutils.utils.eventbus.events;

import com.spiritlight.fishutils.utils.eventbus.events.Event;

import java.util.UUID;

public interface EventBusAccessor {
    void sign(Event event, UUID identifier);

    boolean signed(Event event, UUID identifier);
}
