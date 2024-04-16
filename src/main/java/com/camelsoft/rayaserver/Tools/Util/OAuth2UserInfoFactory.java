package com.camelsoft.rayaserver.Tools.Util;






import com.camelsoft.rayaserver.Enum.Provider;
import com.camelsoft.rayaserver.Models.Ooath.FacebookOAuth2UserInfo;
import com.camelsoft.rayaserver.Models.Ooath.GoogleOAuth2UserInfo;
import com.camelsoft.rayaserver.Models.Ooath.OAuth2UserInfo;
import com.camelsoft.rayaserver.Tools.Exception.OAuth2AuthenticationProcessingException;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(Provider.google.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(Provider.facebook.toString())) {
            return new FacebookOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}