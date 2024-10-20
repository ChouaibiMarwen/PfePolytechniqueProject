package com.smarty.pfeserver.Tools.Util;


import com.smarty.pfeserver.Request.auth.DeviceInfo;
import com.smarty.pfeserver.Response.Auth.OnUserLogoutSuccessEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
@Component
public class OnUserLogoutSuccessEventListener implements ApplicationListener<OnUserLogoutSuccessEvent> {
    private static final Logger logger = LoggerFactory.getLogger(OnUserLogoutSuccessEventListener.class);

    private final LoggedOutJwtTokenCache tokenCache;

    @Autowired
    public OnUserLogoutSuccessEventListener(LoggedOutJwtTokenCache tokenCache) {
        this.tokenCache = tokenCache;
    }

    public void onApplicationEvent(OnUserLogoutSuccessEvent event) {
        if (null != event) {
            DeviceInfo deviceInfo = event.getLogOutRequest().getDeviceInfo();
            logger.info(String.format("Log out success event received for user [%s] for device [%s]", event.getUserEmail(), deviceInfo));
            tokenCache.markLogoutEventForToken(event);
        }
    }
}
