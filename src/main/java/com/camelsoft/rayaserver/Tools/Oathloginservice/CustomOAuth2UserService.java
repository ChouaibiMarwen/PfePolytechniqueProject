package com.camelsoft.rayaserver.Tools.Oathloginservice;


import com.camelsoft.rayaserver.Enum.Provider;
import com.camelsoft.rayaserver.Models.Auth.Role;
import com.camelsoft.rayaserver.Models.Auth.users;
import com.camelsoft.rayaserver.Models.File.File_model;
import com.camelsoft.rayaserver.Models.Ooath.OAuth2UserInfo;
import com.camelsoft.rayaserver.Repository.Auth.RoleRepository;
import com.camelsoft.rayaserver.Repository.Auth.UserRepository;
import com.camelsoft.rayaserver.Services.auth.UserService;
import com.camelsoft.rayaserver.Tools.Exception.OAuth2AuthenticationProcessingException;
import com.camelsoft.rayaserver.Tools.Util.OAuth2UserInfoFactory;
import com.camelsoft.rayaserver.Tools.Util.UserPrincipal;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final Log logger = LogFactory.getLog(CustomOAuth2UserService.class);

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            logger.error("loadUser");
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            logger.error("Exception" + ex.getMessage()+" ex.getCause()"+ ex.getCause());

            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }
    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        users userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        users user;
        if(userOptional!=null) {
            user = userOptional;
//            if(!user.getProvider().equals(Provider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
//                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
//                        user.getProvider() + " account. Please use your " + user.getProvider() +
//                        " account to login.");
//            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private users registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        users user = new users();
        Role userRole = roleRepository.findByRole("ROLE_USER");
        List<users> usersList = this.userService.findAll();
        Long lastuserid = usersList.get(usersList.size() - 1).getId() + 1;
        String username = userService.GenerateUserName(oAuth2UserInfo.getName(), lastuserid);

        user.setProvider(Provider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setUsername(username);
        user.setActive(true);
        user.setRole(userRole);
        File_model file_model = new File_model();
        file_model.setUrl(oAuth2UserInfo.getImageUrl());
        user.setImage(file_model);
        return userRepository.save(user);
    }

    private users updateExistingUser(users existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setName(oAuth2UserInfo.getName());
        File_model file_model = new File_model();
        file_model.setUrl(oAuth2UserInfo.getImageUrl());
        existingUser.setImage(file_model);;
        return userRepository.save(existingUser);
    }

}
