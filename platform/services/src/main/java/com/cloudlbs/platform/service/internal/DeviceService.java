package com.cloudlbs.platform.service.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudlbs.core.utils.dao.JpaGenericDao;
import com.cloudlbs.core.utils.protocol.MessageConverter;
import com.cloudlbs.core.utils.remote.SecureRestTemplate;
import com.cloudlbs.platform.core.LocalOrRemoteService;
import com.cloudlbs.platform.domain.Device;
import com.cloudlbs.platform.protocol.DeviceProto.DeviceMessage;

@Service
public class DeviceService extends
		LocalOrRemoteService<DeviceMessage, Device, Long> {

	public static final String SERVICE_NAME_KEY = "device";

	@Autowired
	public DeviceService(JpaGenericDao<Device, Long> deviceDao,
			SecureRestTemplate secureRestTemplate,
			MessageConverter<DeviceMessage, Device> deviceMessageConverter,
			SystemPropertyService systemPropertyService) {
		super(deviceDao, secureRestTemplate, deviceMessageConverter,
				systemPropertyService, DeviceMessage.items);
	}

	@Override
	public String getServiceNameSysPropKey() {
		return SERVICE_NAME_KEY;
	}

}
