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
 * @see EventBusSubscriber#value()
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventBusSubscriber {
    /**
     * The event class this method is interested in
     * @return the classes and subclasses this event may
     * be interested in
     * @apiNote if this field is set, the bus will only fire
     * events of the provided classes to the method
     */
    Class<? extends Event>[] value() default {};
}
