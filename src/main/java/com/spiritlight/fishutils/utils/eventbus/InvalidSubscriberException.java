package com.spiritlight.fishutils.utils.eventbus;

/**
 * Thrown when the subscribing object to {@link EventBus}
 * has mismatching types (that is, the parameter is not an instance
 * of {@link com.spiritlight.fishutils.utils.eventbus.events.Event}),
 * or if the object does not contain any subscriber methods.
 */
public class InvalidSubscriberException extends RuntimeException {
    public InvalidSubscriberException() {

    }

    public InvalidSubscriberException(String s) {
        super(s);
    }
}
