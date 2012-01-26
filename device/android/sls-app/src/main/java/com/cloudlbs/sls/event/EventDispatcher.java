package com.cloudlbs.sls.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.util.Log;

/**
 * This is a singleton class (created by calling {@link #initialize()}), and is
 * the central event bus of the SLS API. Since any listeners must be registered
 * with it, it must be started up before any other services via a call to
 * {@link #initialize()}<br/>
 * <br/>
 * This implementation will allow any number of listeners, but it loops through
 * all the registered event types on each message. A large number of listeners
 * for a disparate set of fine-grained event types will probably bog this event
 * bus down.
 * 
 * @author Dan Mascenik
 * 
 */
public class EventDispatcher {

	private Map<Class<? extends SLSEvent>, List<Listener>> listenerCache = new HashMap<Class<? extends SLSEvent>, List<Listener>>();
	private Set<Object> lsnrObjCache = new HashSet<Object>();

	private static EventDispatcher instance;

	public static synchronized void initialize() {
		if (instance == null) {
			instance = new EventDispatcher();
		}
	}

	/**
	 * Add any object that implements a listener method (method name begins with
	 * "on" and takes an {@link SLSEvent} as its only argument. Any other
	 * methods will be ignored. Only those methods whose event argument can be
	 * cast from the incoming event will be called.<br/>
	 * <br/>
	 * Note that the listening class doesn't need to implement any interfaces.
	 * Any visible "on...(SLSEvent)" method returning void will do.<br/>
	 * <br/>
	 * Adding a listener multiple times has no effect.
	 * 
	 * @param listener
	 */
	public synchronized static void addListener(Object listener) {
		if (instance.lsnrObjCache.contains(listener)) {
			// already listening
			return;
		}
		Method[] methods = listener.getClass().getMethods();
		for (Method m : methods) {
			if (m.getName().matches("^on[A-Z].*")
					&& m.getReturnType() == void.class
					&& m.getParameterTypes().length == 1
					&& SLSEvent.class
							.isAssignableFrom(m.getParameterTypes()[0])) {

				// This is a valid event listener method
				@SuppressWarnings("unchecked")
				Class<? extends SLSEvent> evtClass = (Class<? extends SLSEvent>) m
						.getParameterTypes()[0];

				List<Listener> lsnrs = instance.listenerCache.get(evtClass);
				if (lsnrs == null) {
					lsnrs = new ArrayList<Listener>();
					instance.listenerCache.put(evtClass, lsnrs);
				}
				lsnrs.add(instance.new Listener(m, listener));
			}
		}
		instance.lsnrObjCache.add(listener);
	}

	/**
	 * Remove a listener object.
	 * 
	 * @param listener
	 */
	public synchronized static void removeListener(Object listener) {
		if (!instance.lsnrObjCache.contains(listener)) {
			// not a registered listener
			return;
		}
		Set<Class<? extends SLSEvent>> keys = instance.listenerCache.keySet();
		@SuppressWarnings("unchecked")
		Class<? extends SLSEvent>[] keyArray = keys.toArray(new Class[0]);
		for (int j = 0; j < keyArray.length; j++) {
			List<Listener> lsnrs = instance.listenerCache.get(keyArray[j]);
			for (int i = 0; i < lsnrs.size(); i++) {
				Listener lsnr = lsnrs.get(i);
				if (lsnr.target == listener) {
					lsnrs.remove(i);
					if (lsnrs.size() == 0) {
						instance.listenerCache.remove(keyArray[j]);
					}
				}
			}
		}
		instance.lsnrObjCache.remove(listener);
	}

	/**
	 * Calls {@link EventListener#handleEvent(SLSEvent)} for any listener
	 * returning true for {@link EventListener#canHandleEvent(SLSEvent)}. For a
	 * single event, multiple listeners will be notified in the order the
	 * listeners were registered. Each listener's handleEvent method is called
	 * in the main thread, so try to avoid long-running event handlers.
	 * 
	 * @param event
	 */
	public static void dispatchEvent(final SLSEvent event) {
		if (instance == null) {
			try {
				Log.w("sls-core", "EventDispatcher is not initialized - "
						+ "dropping event");
			} catch (Exception e) {
				// nevermind
			}
			// do nothing
		} else {
			instance.doEventDispatch(event);
		}
	}

	private void doEventDispatch(final SLSEvent event) {

		/*
		 * Dispatch to the listeners
		 */
		Class<?>[] evtClasses = listenerCache.keySet().toArray(new Class<?>[0]);
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
				} catch (Exception e) {
					try {
						Log.e("sls-core", e.getClass().getSimpleName() + ": "
								+ e.getMessage());
					} catch (Exception re) {
						// nevermind
					}
					e.printStackTrace();
				}
			}

		}
	}

	public static void destroy() {
		instance = null;
	}

	private class Listener {
		private Method method;
		private Object target;

		Listener(Method method, Object target) {
			this.method = method;
			this.target = target;
		}
	}
}
