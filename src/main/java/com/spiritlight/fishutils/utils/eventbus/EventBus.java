package com.spiritlight.fishutils.utils.eventbus;

import com.spiritlight.fishutils.action.ActionResult;
import com.spiritlight.fishutils.utils.eventbus.events.Event;
import com.spiritlight.fishutils.utils.eventbus.events.EventBusAccessor;
import com.spiritlight.fishutils.utils.eventbus.events.EventBusSubscriber;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * An event bus which fires events to listeners.
 * <p></p>
 * The bus provides several utility methods to
 * listen to certain events and to fire them,
 * only the bus subscribers may find them.
 * <p></p>
 * All methods that are to receive events from the event bus
 * should be annotated with {@link com.spiritlight.fishutils.utils.eventbus.events.EventBusSubscriber}
 * in order for the bus to acknowledge the class and fire
 * appropriate event.
 */
public class EventBus {

    private static final Set<UUID> registeredInstances = new CopyOnWriteArraySet<>();
    private final UUID identifier;

    private final List<Object> subscribers = new CopyOnWriteArrayList<>();
    private final Set<EventBus> inheritances = new CopyOnWriteArraySet<>();

    /**
     * whether the bus should be able to receive the same event
     * multiple times. If this is {@code false}, this bus will
     * not write a signature to the event.
     */
    private final boolean processDuplicates;

    /**
     * The shared event bus instance
     */
    public static final EventBus INSTANCE = new EventBus();

    /**
     * Creates an event bus
     * @param processDuplicates whether this bus should be able to
     *                     receive the same event multiple times
     * @apiNote "same event" in this context does not indicate that
     * for event a and b, if {@code a.equals(b)} returns true, the
     * event is discarded, but rather if {@code a == b} is true,
     * the event is discarded.
     * In other words, the event is discarded if and only if {@code processDuplicates}
     * is set to false (default) and if the two events have identical identity.
     */
    public EventBus(boolean processDuplicates) {
        UUID identifier;
        // Keeps fetching until there are no duplicating identifier.
        //noinspection StatementWithEmptyBody
        while(registeredInstances.contains((identifier = UUID.randomUUID())));
        registeredInstances.add(identifier);
        this.identifier = identifier;
        this.processDuplicates = processDuplicates;
    }

    public EventBus() {
        this(false);
    }

    public void subscribe(Object object) throws InvalidSubscriberException {
        check(object);
        subscribers.add(object);
    }

    public void unsubscribe(Object object) {
        subscribers.remove(object);
    }

    /**
     * Hooks this event bus to a certain parent, listening
     * to all events from the said parent.
     * <p></p>
     * This method does a deep recursion check to see whether
     * the parent inherits from this bus, if so, an {@link IllegalStateException}
     * will be thrown.
     *
     * @param parent the parent
     * @apiNote This will add the current bus instance to the
     * parent's inheritance set, in which is called every time
     * the parent fires an event. In other words, this bus will
     * receive every event the parent receives. In some structures,
     * the event may be received multiple times. If this is intended,
     * the event bus should be created with {@link EventBus#EventBus(boolean)}}
     * and with "processMulti" set to true.
     */
    public void hook(EventBus parent) {
        if(parent == this) throw new IllegalStateException("cannot inherit from self");
        recursionCheck(Objects.requireNonNull(parent) /* Implicit null check */);
        parent.inheritances.add(this);
    }

    public void uninherit(EventBus parent) {
        parent.inheritances.remove(this);
    }

    public void fire(Event event) {
        if(!this.processDuplicates) {
            EventBusAccessor accessor = Secret.getAccessor();
            if(accessor.signed(event, identifier)) return;
            accessor.sign(event, identifier);
        }
        subscribers.forEach(s -> fire(s, event));
        inheritances.forEach(bus -> bus.fire(event));
    }

    private void recursionCheck(EventBus parent) {
        if(this.inheritances.contains(parent)) throw new IllegalStateException("recursive inheritance");
        this.inheritances.forEach(this::recursionCheck);
    }

    private static void fire(Object o, Event event) {
        EventBusSubscriber a; Class<?> c;
        Method[] methods;
        Object invocationTarget;
        if(o instanceof Class<?> cls) {
            invocationTarget = null;
            methods = cls.getMethods();
        } else {
            invocationTarget = o;
            methods = o.getClass().getDeclaredMethods();
        }

        for(Method method : methods) {
            condition:
            if((a = method.getAnnotation(EventBusSubscriber.class)) != null &&
               method.getParameterCount() == 1 &&
               method.getParameterTypes()[0].isAssignableFrom((c = event.getClass()))) {
                method.setAccessible(true);
                if(a.value().length == 0 && a.only().length == 0) {
                    ActionResult.run(() -> method.invoke(invocationTarget, event));
                } else {
                    for(Class<?> clazz : a.only()) {
                        if(clazz == c) {
                            ActionResult.run(() -> method.invoke(invocationTarget, event));
                            break condition;
                        }
                    }
                    for(Class<?> clazz : a.value()) {
                        if(clazz.isAssignableFrom(c)) {
                            ActionResult.run(() -> method.invoke(invocationTarget, event));
                            break condition;
                        }
                    }
                }
            }
        }
    }

    private static void check(Object o) {
        EventBusSubscriber a; Class<?> c; int cnt;
        Method[] methods = o instanceof Class<?> ? ((Class<?>) o).getMethods() : o.getClass().getDeclaredMethods();
        for(Method method : methods) {
            if((a = method.getAnnotation(EventBusSubscriber.class)) != null) {
                // only one parameter for a listening method
                if((cnt = method.getParameterCount()) != 1) {
                    throw new InvalidSubscriberException("annotated method " + method + " has " + cnt + " parameters, expected 1");
                }

                if(Event.class.isAssignableFrom((c = method.getParameterTypes()[0]))) {
                    for(Class<?> clazz : a.value()) {
                        // parameter is not event
                        if(!Event.class.isAssignableFrom(clazz)) {
                            throw new InvalidSubscriberException("class " + clazz + " in annotated member " + method + " is not an event type");
                        }
                        // listening to other classes other than parent is a no no
                        if(!c.isAssignableFrom(clazz)) {
                            throw new InvalidSubscriberException("class " + clazz + " is incompatible for listening event type " + c);
                        }
                    }
                    for(Class<?> clazz : a.only()) {
                        // parameter is not event
                        if(!Event.class.isAssignableFrom(clazz)) {
                            throw new InvalidSubscriberException("class " + clazz + " in annotated member " + method + " is not an event type");
                        }
                        // listening to other classes other than parent is a no no
                        if(!c.isAssignableFrom(clazz)) {
                            throw new InvalidSubscriberException("class " + clazz + " is incompatible for listening event type " + c);
                        }
                    }
                    return; // yay!!! we found one, this qualifies as a subscriber then
                } else {
                    throw new InvalidSubscriberException("class " + c + " is not an instance of Event");
                }
            }
        }
        // Deprecated since 1.2
        // throw new InvalidSubscriberException("class " + o.getClass() + " does not have any subscriber methods");
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        EventBus eventBus = (EventBus) object;
        return Objects.equals(identifier, eventBus.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }
}
