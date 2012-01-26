package com.cloudlbs.sls.ui.mvp;

import java.util.HashMap;
import java.util.Map;

/**
 * There should be no more than one instance of a type of {@link Model} per VM.
 * Given the needed model class, this will return the instance of it, or create
 * a new one if none exists yet.
 * 
 * @author Dan Mascenik
 * 
 */
public class ModelLocator {

	private static Map<Class<? extends Model>, Model> models = new HashMap<Class<? extends Model>, Model>();

	public synchronized static <M extends Model> M getModel(Class<M> key) {
		@SuppressWarnings("unchecked")
		M model = (M) models.get(key);
		if (model == null) {
			try {
				model = key.newInstance();
			} catch (Exception e) {
				throw new RuntimeException("Could not create model class "
						+ key.getName(), e);
			}
			models.put(key, model);
		}
		return model;
	}
}
