package com.cloudlbs.web.pub.client.rpc;

import com.cloudlbs.web.pub.shared.model.UsernamePasswordAuthentication;
import com.cloudlbs.web.pub.shared.model.NewUserDetails;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("user")
public interface RPCUserService extends RemoteService {

    boolean login(UsernamePasswordAuthentication credentials);
    boolean createUser(NewUserDetails details);

}
