package com.spiritlight.fishutils.utils.eventbus.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for the event bus to know which event to fire to.
 * <p></p>
 * The annotated method must have only one parameter, and it
 * should extend from the {@link Event} class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventBusSubscriber {
}
