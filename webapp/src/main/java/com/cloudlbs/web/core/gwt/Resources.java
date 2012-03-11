package com.cloudlbs.web.core.gwt;

import com.cloudlbs.web.core.gwt.css.CloudLBSStyles;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {

    @Source("img/working-spinner-small.gif")
    ImageResource workingSpinner();

    @Source("img/spacer.gif")
    ImageResource spacer();

    @Source("css/GlobalStyles.css")
    CloudLBSStyles css();

}
