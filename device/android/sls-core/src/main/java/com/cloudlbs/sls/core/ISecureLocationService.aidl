package com.cloudlbs.sls.core;

// ----------------------------------------------------------
//
// This is the published interface for the Secure Location 
// Service on the Android platform. It is the device-side
// access point to location services.
//
// ----------------------------------------------------------

import com.cloudlbs.sls.core.LocationData;
import com.cloudlbs.sls.core.AuthenticationStatus;
import com.cloudlbs.sls.core.SLSStatus;
import com.cloudlbs.sls.core.LocationRequestParams;

interface ISecureLocationService {

    /**
     * Returns true if the SLS is enabled, whether or not it currently 
     * has a network connection or other fault condition.
     */
	boolean isEnabled();

    /**
	 * Gets the status of the SLS. Various status codes are provided
	 * as static constants within the SLSStatus class.
     */
    SLSStatus getStatus();
    
    /**
     * Indicates if the app is currently authenticated with the SLS using 
     * the provided username.  This does not go out over the network. It 
     * just verifies the provided credentials against those cached in the 
     * SLS. The SLS cache may expire at any point, causing this method to 
     * return false. If using provider-based authentication, the username
     * should be left null.
     */
    boolean isAuthenticated(String apiKey, String username);
    
    /**
     * Log in with the user's CloudLBS credentials.  This provides access 
     * to user-to-user sharing in the user's location sharing network. The
     * app's API key is also required to validate the calling app.
     */
    AuthenticationStatus authenticate(String apiKey, String username, String password);
    
    /**
     * Given a valid API key, clears the app from the SLS such that 
     * the app is no longer able to receive push data from the backend or make 
     * any calls to the CloudLBS library or web service.
     */
    void logout(String apiKey);
    
    /**
     * This is a versatile call for retrieving a single reading or recurring 
     * readings from the device GPS or network location provider. This call 
     * returns a correlation ID for schedule management purposes. When an
     * acceptable location reading is obtained by the SLS, a broadcast with the
     * correlation ID is sent. The app is responsible for calling back to
     * receive the location data. This avoids broadcasting private location
     * data to other potentially unauthenticated apps.
     */
    int getLocation(String apiKey, in LocationRequestParams params);
    
    /**
     * Returns true if the app has a scheduled reading with recurrence.
     */
    boolean isPinging(String apiKey);
    
    /**
     * Cancels any scheduled readings for the app, terminating any pinging.
     */
    void cancelReadings(String apiKey);
    
    /**
     * Get any data the SLS has for the app. This would be called in
     * response to a broadcast from the SLS to the app.
     */
    List<LocationData> getData(String apiKey);
}
