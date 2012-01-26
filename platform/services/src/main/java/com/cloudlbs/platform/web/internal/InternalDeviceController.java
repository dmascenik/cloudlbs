package com.cloudlbs.platform.web.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cloudlbs.core.utils.web.GenericController;
import com.cloudlbs.platform.core.InternalGenericService;
import com.cloudlbs.platform.domain.Device;
import com.cloudlbs.platform.protocol.DeviceProto.DeviceMessage;

/**
 * Internal-only controller
 * 
 */
@Controller
@RequestMapping("/internal/device")
public class InternalDeviceController extends
		GenericController<DeviceMessage, Device> {

	@Autowired
	public InternalDeviceController(
			InternalGenericService<DeviceMessage, Device> deviceService) {
		super(deviceService, deviceService.getMessageConverter(), deviceService
				.getMessageTypeExtension());
	}

	@Override
	protected boolean isPreauthenticationAllowed() {
		return true;
	}
}
