package com.manager.timezone.timezonemanagerserver.service.impl;

import com.manager.timezone.timezonemanagerserver.auth.AuthUserDetails;
import com.manager.timezone.timezonemanagerserver.model.User;
import com.manager.timezone.timezonemanagerserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username : " + username);
        }

        return new AuthUserDetails(optionalUser.get());
    }

    public UserDetails loadUserById(UUID uid) {
        User user = userRepository.findById(uid)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID : " + uid));

        return new AuthUserDetails(user);
    }
}
