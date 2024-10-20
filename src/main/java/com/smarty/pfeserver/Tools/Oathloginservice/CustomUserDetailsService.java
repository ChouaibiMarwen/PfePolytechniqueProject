package com.smarty.pfeserver.Tools.Oathloginservice;



import com.smarty.pfeserver.Models.User.users;
import com.smarty.pfeserver.Repository.User.UserRepository;
import com.smarty.pfeserver.Tools.Exception.ResourceNotFoundException;
import com.smarty.pfeserver.Tools.Util.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        try {
            users user = userRepository.findByEmail(email);

            return UserPrincipal.create(user);
        }catch (NoSuchElementException ex){
            throw  new ResourceNotFoundException("User"+"email"+email);
        }

    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        users user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User"+"id: "+ id)
        );

        return UserPrincipal.create(user);
    }
}