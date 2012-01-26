package com.cloudlbs.core.utils.events;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * This event bus is similar to the one used on devices, but uses the Spring
 * Framework to add listeners and maintain its singularity.<br/>
 * <br/>
 * This implementation will allow any number of listeners, but it loops through
 * all the registered event types on each message. A large number of listeners
 * for a disparate set of fine-grained event types will probably bog this event
 * bus down.
 * 
 * @author Dan Mascenik
 * 
 */
public class EventDispatcher implements InitializingBean {

	private List<Object> listeners;

	private Map<Class<? extends Event>, List<Listener>> listenerCache = new HashMap<Class<? extends Event>, List<Listener>>();

	/**
	 * Add any object that implements a listener method (method name begins with
	 * "on" and takes an {@link Event} as its only argument. Any other methods
	 * will be ignored. Only those methods whose event argument can be cast from
	 * the incoming event will be called.<br/>
	 * <br/>
	 * Note that the listening class doesn't need to implement any interfaces.
	 * Any visible "on...(Event)" method returning void will do.<br/>
	 * <br/>
	 * Adding a listener multiple times has no effect.
	 * 
	 * @param listener
	 */
	private void addListener(Object listener) {
		Method[] methods = listener.getClass().getMethods();
		for (Method m : methods) {
			if (m.getName().matches("^on[A-Z].*")
					&& m.getReturnType() == void.class
					&& m.getParameterTypes().length == 1
					&& Event.class.isAssignableFrom(m.getParameterTypes()[0])) {

				// This is a valid event listener method
				@SuppressWarnings("unchecked")
				Class<? extends Event> evtClass = (Class<? extends Event>) m
						.getParameterTypes()[0];

				List<Listener> lsnrs = listenerCache.get(evtClass);
				if (lsnrs == null) {
					lsnrs = new ArrayList<Listener>();
					listenerCache.put(evtClass, lsnrs);
				}
				lsnrs.add(new Listener(m, listener));
			}
		}
	}

	/**
	 * Calls {@link EventListener#handleEvent(Event)} for any listener returning
	 * true for {@link EventListener#canHandleEvent(Event)}. For a single event,
	 * multiple listeners will be notified in the order the listeners were
	 * registered. Each listener's handleEvent method is called in the main
	 * thread, so try to avoid long-running event handlers.
	 * 
	 * @param event
	 */
	public void dispatchEvent(Event event) {
		doEventDispatch(event);
	}

	private void doEventDispatch(Event event) {

		/*
		 * Dispatch to the listeners
		 */
		Class<?>[] evtClasses = listenerCache.keySet().toArray(new Class<?>[0]);
		int d = 0;
		for (int j = 0; j < evtClasses.length; j++) {
			Class<?> evtClass = evtClasses[j];
			if (!evtClass.isAssignableFrom(event.getClass())) {
				// Not valid for this event type
				continue;
			}

			List<Listener> listeners = listenerCache.get(evtClass);
			for (int i = 0; i < listeners.size(); i++) {
				final Listener lsnr = listeners.get(i);
				try {
					lsnr.method.invoke(lsnr.target, event);
					d++;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		log.debug(event.getClass().getName() + " dispatched to " + d
				+ " listeners");
	}

	private class Listener {
		private Method method;
		private Object target;

		Listener(Method method, Object target) {
			this.method = method;
			this.target = target;
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		for (Object listener : listeners) {
			addListener(listener);
		}
	}

	public void setListeners(List<Object> listeners) {
		this.listeners = listeners;
	}

	private Logger log = LoggerFactory.getLogger(EventDispatcher.class);

}
