package com.cloudlbs.sls.event;

import com.cloudlbs.sls.location.CancelReadingRequest;
import com.cloudlbs.sls.location.ReadingRequest;

/**
 * @author Dan Mascenik
 * 
 */
public interface ReadingRequestListener {

	public void onReadingRequest(ReadingRequest readingRequest);

	public void onCancelReadingRequest(CancelReadingRequest cancelRequest);

}
