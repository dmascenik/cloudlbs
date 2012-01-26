package com.cloudlbs.sls;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.TelephonyManager;

import com.cloudlbs.sls.core.SLSServiceConnection;
import com.cloudlbs.sls.dao.AndroidOutboundMessageDaoImpl;
import com.cloudlbs.sls.dao.AndroidPhoneInfoDaoImpl;
import com.cloudlbs.sls.dao.AndroidPreferencesDaoImpl;
import com.cloudlbs.sls.dao.OutboundMessageDao;
import com.cloudlbs.sls.dao.PhoneInfoDao;
import com.cloudlbs.sls.dao.PreferencesDao;
import com.cloudlbs.sls.event.EventDispatcher;
import com.cloudlbs.sls.event.NetworkStatusEvent;
import com.cloudlbs.sls.event.ShutdownEvent;
import com.cloudlbs.sls.event.StartupEvent;
import com.cloudlbs.sls.http.AppDetailsRemoteService;
import com.cloudlbs.sls.http.AuthenticationRemoteService;
import com.cloudlbs.sls.http.DeviceRegistrationRemoteService;
import com.cloudlbs.sls.location.AndroidLocationDetectorManager;
import com.cloudlbs.sls.location.LocationProcessor;
import com.cloudlbs.sls.location.LocationReadingSender;
import com.cloudlbs.sls.location.ReadingSchedule;
import com.cloudlbs.sls.location.ScheduleManager;
import com.cloudlbs.sls.utils.CredentialsCache;
import com.cloudlbs.sls.utils.Logger;
import com.cloudlbs.sls.utils.MessageConverterRegistry;
import com.cloudlbs.sls.xmpp.AndroidProcessorClient;
import com.cloudlbs.sls.xmpp.InboundXmppMessageHandler;
import com.cloudlbs.sls.xmpp.ProcessorClient;

/**
 * Provides all Android lifecycle methods associated with the {@link SLSService}
 * . This class is mainly intended to improve readability of the
 * {@link SLSService} code by reducing that strictly to service methods.
 * 
 * @see SLSService
 * 
 * @author Dan Mascenik
 */
public abstract class BaseSLSService extends Service {

	public static final String SYSTEM_IDENTIFIER = "SYSTEM";
	public static final String LOCAL_SERVICE_ACTION = "com.cloudlbs.sls.LOCAL_SERVICE";

	protected LogBuffer logBuffer;
	protected ProcessorClient processorClient;
	protected PreferencesDao preferencesDao;
	protected PhoneInfoDao phoneInfoDao;
	private OutboundMessageDao outboundMessageDao;
	private DeviceRegistrationRemoteService deviceConnectionRemoteService;
	protected AuthenticationRemoteService authenticationRemoteService;
	protected AppDetailsRemoteService appDetailsRemoteService;
	protected ScheduleManager manager;
	protected LocationDataBuffer locationDataBuffer;
	protected CredentialsCache credentialsCache;

	/**
	 * Listens for network status events and notifies any listeners
	 */
	protected BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.content.BroadcastReceiver#onReceive(android.content.Context,
		 * android.content.Intent)
		 */
		@Override
		public void onReceive(Context context, Intent intent) {
			boolean isNetworkAvailable = !intent.getBooleanExtra(
					ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
			NetworkStatusEvent evt = new NetworkStatusEvent(isNetworkAvailable);
			if (isNetworkAvailable) {
				Logger.info("Network returned");
			} else {
				Logger.info("Network dropped");
			}
			EventDispatcher.dispatchEvent(evt);
		}

	};

	/**
	 * Service {@link #onCreate()} can be called multiple times from different
	 * threads. This ensures that only one set of resources actually gets
	 * created.
	 */
	private static boolean isAlive;

	// -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	//
	// Local and remote service bindings, instantiated in the
	// onCreate method.
	//
	// -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	protected LocalServiceBinder localBinder;
	protected SLSServiceRemote remoteBinder;

	// -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	//
	// Android capabilities used by the service.
	//
	// -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	private ConnectivityManager connectivityManager;
	private LocationManager locationManager;
	private TelephonyManager telephonyManager;
	// private SensorManager sensorManager;

	// -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	//
	// SLS startup and shutdown methods. Startup is called from
	// onCreate if the SLS is enabled.
	//
	// -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-

	protected boolean isRunning;

	// -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	//
	// Android lifecycle methods
	//
	// -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-

	/**
	 * Initializes the {@link EventDispatcher}, the {@link SLSController}, and
	 * the local and remote binding classes. Once all this is complete, send out
	 * a message to the server that the SLS is online.
	 * 
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public synchronized void onCreate() {
		if (!isAlive) {
			preferencesDao = new AndroidPreferencesDaoImpl(this);

			logBuffer = new LogBuffer();
			logBuffer.setSize(preferencesDao.getLogBufferSize());

			boolean enabled = preferencesDao.getEnabled();
			Logger.debug("Initial enabled setting is " + enabled);

			connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
			locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

			phoneInfoDao = new AndroidPhoneInfoDaoImpl(telephonyManager);
			outboundMessageDao = new AndroidOutboundMessageDaoImpl(this);
			deviceConnectionRemoteService = new DeviceRegistrationRemoteService(
					preferencesDao);
			authenticationRemoteService = new AuthenticationRemoteService(
					preferencesDao);
			appDetailsRemoteService = new AppDetailsRemoteService(
					preferencesDao);
			locationDataBuffer = new LocationDataBuffer();
			credentialsCache = new CredentialsCache();

			localBinder = new LocalServiceBinder((SLSService) this);
			remoteBinder = new SLSServiceRemote((SLSService) this);

			new MessageConverterRegistry();
			new LocationReadingSender();
			new LocationDataBroadcaster(this, locationDataBuffer);
			new InboundXmppMessageHandler();

			processorClient = new AndroidProcessorClient(
					deviceConnectionRemoteService, phoneInfoDao,
					connectivityManager, preferencesDao, outboundMessageDao);

			manager = new ScheduleManager();
			ReadingSchedule schedule = manager.getSchedule();
			new AndroidLocationDetectorManager(new LocationProcessor(schedule),
					locationManager);

			if (enabled) {
				registerReceiver(connectivityReceiver, new IntentFilter(
						ConnectivityManager.CONNECTIVITY_ACTION));
				EventDispatcher.dispatchEvent(new StartupEvent());
			}
			isAlive = true;
		}
	}

	@Override
	public synchronized void onDestroy() {
		if (isAlive) {
			EventDispatcher.dispatchEvent(new ShutdownEvent());
			isAlive = false;
		}
	}

	/**
	 * Returns true if there is a network connection
	 */
	protected boolean isNetworkAvailable() {
		if (connectivityManager == null) {
			return false;
		}
		NetworkInfo net = connectivityManager.getActiveNetworkInfo();
		return (net != null && net.isConnected());
	}

	/**
	 * Returns a {@link LocalServiceBinder} or an {@link SLSServiceRemote},
	 * depending on the Intent action.
	 * 
	 * @see android.app.Service#onBind(Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {

		if (intent.getAction().equals(LOCAL_SERVICE_ACTION)) {
			Logger.debug("Local service being bound");
			return localBinder;
		}
		if (intent.getAction().equals(
				SLSServiceConnection.REMOTE_SERVICE_ACTION)) {
			Logger.debug("Remote service being bound");
			return remoteBinder;
		}
		throw new RuntimeException("Unknown intent action: "
				+ intent.getAction());
	}

	/**
	 * A local binding class that simply returns the concrete instance of the
	 * {@link SLSService}. This provides direct access to all the service
	 * methods, including the ones that should not be exposed to other
	 * processes.
	 * 
	 * @author Dan Mascenik
	 * 
	 */
	public class LocalServiceBinder extends Binder {

		private SLSService secLocService;

		LocalServiceBinder(SLSService secLocService) {
			this.secLocService = secLocService;
		}

		public SLSService getService() {
			return secLocService;
		}

	}

}
