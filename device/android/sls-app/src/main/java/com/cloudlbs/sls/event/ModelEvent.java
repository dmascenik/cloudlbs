package com.cloudlbs.sls.event;

import com.cloudlbs.sls.ui.mvp.Model;

/**
 * @author Dan Mascenik
 * 
 */
public class ModelEvent implements SLSEvent {

	private Model model;
	private boolean noLogging;

	public ModelEvent(Model model) {
		this.model = model;
	}

	public ModelEvent(Model model, boolean noLogging) {
		this(model);
		this.noLogging = noLogging;
	}

	public Model getModel() {
		return model;
	}

	public void noLogging() {
		this.noLogging = true;
	}

	public boolean isNoLogging() {
		return noLogging;
	}

	public Class<? extends Model> getModelClass() {
		return model.getClass();
	}
}
