package com.cloudlbs.sls.event;


/**
 * 
 * @author Dan Mascenik
 * 
 */
public interface ActivationListener {

	public void onShutdown(ShutdownEvent evt);

	public void onStartupEvent(StartupEvent evt);

}
