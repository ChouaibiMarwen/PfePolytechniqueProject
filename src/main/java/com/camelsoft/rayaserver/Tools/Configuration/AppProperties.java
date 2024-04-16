package com.camelsoft.rayaserver.Tools.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final Auth auth = new Auth();
    private final OAuth2 oauth2 = new OAuth2();

    public static class Auth {
        private String tokenSecret;
        private long tokenExpirationMsec;

        public String getTokenSecret() {
            return tokenSecret;
        }

        public void setTokenSecret(String tokenSecret) {
            this.tokenSecret = tokenSecret;
        }

        public long getTokenExpirationMsec() {
            return tokenExpirationMsec;
        }

        public void setTokenExpirationMsec(long tokenExpirationMsec) {
            this.tokenExpirationMsec = tokenExpirationMsec;
        }
    }

    public static final class OAuth2 {
        private List<String> authorizedRedirectUris = Arrays.asList(
                "https://rayafinancing.com/oauth2/redirect",
                "https://rayafinancing.com/signin/oauth2/redirect",
                "https://server.rayafinancing.com/signin/oauth2/redirect",
                "https://rayafinancing.com/oauth2/callback/google",
                "https://server.rayafinancing.com/oauth2/callback/google",
                "https://localhost:8000/oauth2/callback/google",
                "http://localhost:4200/oauth2/redirect",
                "https://server.rayafinancing.com",
                "https://rayafinancing.com",
                "https://www.rayafinancing.com/oauth2/callback/google",
                "https://rayafinancing.com/oauth2/callback/google",
                "https://www.rayafinancing.com/signin/oauth2/redirect",
                "http://localhost:4200/signin/oauth2/redirect",
                "https://localhost:8000/signin/oauth2/redirect",
                "https://server.rayafinancing.com/oauth2/authorize/google",
                "https://rayafinancing.com/oauth2/authorize/google",
                "https://server.rayafinancing.com/signin/oauth2/redirect"
        );

        public List<String> getAuthorizedRedirectUris() {
            return authorizedRedirectUris;
        }

        public OAuth2 authorizedRedirectUris(List<String> authorizedRedirectUris) {
            this.authorizedRedirectUris = authorizedRedirectUris;
            return this;
        }
    }

    public Auth getAuth() {
        return auth;
    }

    public OAuth2 getOauth2() {
        return oauth2;
    }
}