package com.smarty.pfeserver.Tools.Util;


import com.smarty.pfeserver.Response.Auth.OnUserLogoutSuccessEvent;
import com.smarty.pfeserver.Tools.Exception.InvalidTokenRequestException;
import io.jsonwebtoken.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class TokenUtil {

    private final String CLAIMS_SUBJECT = "sub";
    private final String CLAIMS_CREATED = "created";
    @Autowired
    private LoggedOutJwtTokenCache loggedOutJwtTokenCache;
    @Value("${auth.jwtExpirationMs}")
    private Long TOKEN_VALIDITY = 86400000L;
    @Value("${auth.jwtRefreshExpirationMs}")
    private Long REFRESH_TOKEN_VALIDITY = 86400000L;


    @Value("${auth.secret}")
    private String TOKEN_SECRET;
    private static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIMS_SUBJECT, userDetails.getUsername());
        claims.put(CLAIMS_CREATED, new Date());
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET)
                .compact();
    }

    public String getUserNameFromToken(String token) {
        try {


            Claims claims = getClaims(token);



            if (claims == null && token !=null){
                String[] parts = token.split("\\.");
                String payload = parts[1];
                // Decode the payload from Base64
                byte[] decodedPayload = Base64.getDecoder().decode(payload);
                String payloadString = new String(decodedPayload);
                JSONObject payloadJson = new JSONObject(payloadString);
                // Extract the information you need
                String iss = payloadJson.getString("iss");
                String email = payloadJson.getString("email");
                logger.error(email);
                return email;
            }

            return claims.getSubject();
        }catch (Exception ex) {
            return null;
        }
    }

    public String getUserNameFromTokenlkj(String token) {
        try {



            if (token !=null){
                String[] parts = token.split("\\.");
                String payload = parts[1];
                // Decode the payload from Base64
                byte[] decodedPayload = Base64.getDecoder().decode(payload);
                String payloadString = new String(decodedPayload);
                JSONObject payloadJson = new JSONObject(payloadString);
                // Extract the information you need
                String iss = payloadJson.getString("iss");
                String email = payloadJson.getString("email");

                return email;
            }

            return null;
        }catch (Exception ex) {
            return null;
        }
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + TOKEN_VALIDITY*1000);
    }

    private boolean isTokenExpired(String token) throws JSONException {



        Claims claims = getClaims(token);
        if (claims==null){
            String[] parts = token.split("\\.");
            String payload = parts[1];
            // Decode the payload from Base64
            byte[] decodedPayload = Base64.getDecoder().decode(payload);
            String payloadString = new String(decodedPayload);
            JSONObject payloadJson = new JSONObject(payloadString);
            // Extract the information you need
            Long exp = payloadJson.getLong("exp");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(exp));

            // Add 7 days to the current date
            calendar.add(Calendar.DAY_OF_MONTH, 7);

            // Get the updated date
            Date updatedDate = calendar.getTime();
            return updatedDate.after(new Date());
        }
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) throws JSONException {
        String username = getUserNameFromToken(token);

        return (!isTokenExpired(token));
    }

    private Claims getClaims(String token) {
        Claims claims=null;
        try {

            claims = Jwts.parser().setSigningKey(TOKEN_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception ex) {
            return claims ;
        }

        return claims;
    }

    public Claims getClaimsdd(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(TOKEN_SECRET).parseClaimsJws(token).getBody();

        }catch (Exception ex) {
            claims = Jwts.parser().parseClaimsJws(token).getBody();
        }

        return claims;
    }
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(TOKEN_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
    public Date getTokenExpiryFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(TOKEN_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }
    private void validateTokenIsNotForALoggedOutDevice(String authToken) {
        OnUserLogoutSuccessEvent previouslyLoggedOutEvent = loggedOutJwtTokenCache.getLogoutEventForToken(authToken);
        if (previouslyLoggedOutEvent != null) {
            String userEmail = previouslyLoggedOutEvent.getUserEmail();
            Date logoutEventDate = previouslyLoggedOutEvent.getEventTime();
            String errorMessage = String.format("Token corresponds to an already logged out user [%s] at [%s]. Please login again", userEmail, logoutEventDate);
            throw new InvalidTokenRequestException("JWT", authToken, errorMessage);
        }
    }
}

