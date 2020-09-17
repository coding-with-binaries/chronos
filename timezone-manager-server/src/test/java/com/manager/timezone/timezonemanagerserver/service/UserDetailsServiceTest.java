package com.manager.timezone.timezonemanagerserver.service;

import com.manager.timezone.timezonemanagerserver.model.User;
import com.manager.timezone.timezonemanagerserver.repository.UserRepository;
import com.manager.timezone.timezonemanagerserver.service.impl.UserDetailsServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class UserDetailsServiceTest {
    private static final String USERNAME = "varun";

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl();

    @Mock
    private UserRepository userRepository;

    @Test
    public void test_loadUserByUsername() {
        User user = new User();
        user.setUsername(USERNAME);
        PowerMockito.when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(USERNAME);
        assertEquals(USERNAME, userDetails.getUsername());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void test_loadUserByUsername_Error() {
        PowerMockito.when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        userDetailsService.loadUserByUsername(USERNAME);
    }

    @Test
    public void test_loadUserById() {
        UUID uid = UUID.randomUUID();
        User user = new User();
        user.setUid(uid);
        user.setUsername(USERNAME);
        PowerMockito.when(userRepository.findById(uid)).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserById(uid);
        assertEquals(USERNAME, userDetails.getUsername());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void test_loadUserById_Error() {
        UUID uid = UUID.randomUUID();
        PowerMockito.when(userRepository.findById(uid)).thenReturn(Optional.empty());

        userDetailsService.loadUserById(uid);
    }
}
