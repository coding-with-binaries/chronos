package com.chronos.chronosserver.service;

import com.chronos.chronosserver.auth.AuthUserDetails;
import com.chronos.chronosserver.constants.ChronosConstants;
import com.chronos.chronosserver.dto.*;
import com.chronos.chronosserver.exception.InvalidResourceException;
import com.chronos.chronosserver.exception.UserExistsException;
import com.chronos.chronosserver.model.Role;
import com.chronos.chronosserver.model.User;
import com.chronos.chronosserver.repository.RoleRepository;
import com.chronos.chronosserver.repository.UserRepository;
import com.chronos.chronosserver.auth.JwtTokenProvider;
import com.chronos.chronosserver.exception.OperationForbiddenException;
import com.chronos.chronosserver.exception.ResourceNotFoundException;
import com.chronos.chronosserver.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@PowerMockIgnore("javax.security.*")
@PrepareForTest({SecurityContextHolder.class})
public class UserServiceTest {
    private static final String USERNAME = "varun";
    private static final String PASSWORD = "password";

    @TestConfiguration
    static class UserServiceTestContextConfiguration {
        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
    }

    @Autowired
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider tokenProvider;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    private Role role;

    private User admin;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(SecurityContextHolder.class);
        initializeAuthenticatedAdmin();

        PowerMockito.when(roleRepository.findByType(RoleType.admin)).thenReturn(Optional.of(role));
        PowerMockito.when(userRepository.saveAndFlush(any(User.class))).thenReturn(admin);
        PowerMockito.when(userRepository.save(any(User.class))).thenReturn(admin);
    }

    @Test
    public void test_isUsernameTaken() {
        PowerMockito.when(userRepository.existsByUsername(USERNAME)).thenReturn(true);

        boolean taken = userService.isUsernameTaken(USERNAME);
        assertTrue(taken);
    }

    @Test
    public void test_getCurrentAuthenticatedUser() {
        UserDto userDto = userService.getCurrentAuthenticatedUser();
        assertEquals(USERNAME, userDto.getUsername());
    }

    @Test
    public void test_getCurrentAuthenticatedUser_Unauthenticated() {
        initializeUnauthenticatedUser();

        UserDto userDto = userService.getCurrentAuthenticatedUser();
        assertNull(userDto);
    }

    @Test
    public void test_whoAmI() {
        WhoAmIDto whoAmIDto = userService.whoAmI();
        assertEquals(USERNAME, whoAmIDto.getUsername());
    }

    @Test
    public void test_whoAmI_Unauthenticated() {
        initializeUnauthenticatedUser();

        WhoAmIDto whoAmIDto = userService.whoAmI();
        assertNull(whoAmIDto);
    }

    @Test
    public void test_authenticateUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PowerMockito.when(authenticationManager.authenticate(any())).thenReturn(authentication);

        AuthenticateUserRequestDto authenticateUserRequestDto = new AuthenticateUserRequestDto();
        authenticateUserRequestDto.setUsername(USERNAME);
        authenticateUserRequestDto.setPassword(PASSWORD);
        AuthenticateUserResponseDto authenticateUserResponseDto =
                userService.authenticateUser(authenticateUserRequestDto);

        assertEquals(USERNAME, authenticateUserResponseDto.getUsername());
    }

    @Test
    public void test_registerUser() throws UserExistsException, OperationForbiddenException {

        RegisterUserRequestDto registerUserRequestDto = new RegisterUserRequestDto();
        registerUserRequestDto.setUsername(USERNAME);
        registerUserRequestDto.setPassword(PASSWORD);
        UserDto userDto = userService.registerUser(registerUserRequestDto);

        assertEquals(USERNAME, userDto.getUsername());
    }

    @Test
    public void test_registerUser_Admin() throws UserExistsException, OperationForbiddenException {

        RegisterUserRequestDto registerUserRequestDto = new RegisterUserRequestDto();
        registerUserRequestDto.setUsername(USERNAME);
        registerUserRequestDto.setPassword(PASSWORD);
        registerUserRequestDto.setRole(RoleType.admin);
        UserDto userDto = userService.registerUser(registerUserRequestDto);

        assertEquals(USERNAME, userDto.getUsername());
    }

    @Test(expected = UserExistsException.class)
    public void test_registerUser_UserExists() throws UserExistsException, OperationForbiddenException {
        PowerMockito.when(userRepository.existsByUsername(USERNAME)).thenReturn(true);
        RegisterUserRequestDto registerUserRequestDto = new RegisterUserRequestDto();
        registerUserRequestDto.setUsername(USERNAME);
        userService.registerUser(registerUserRequestDto);
    }


    @Test(expected = OperationForbiddenException.class)
    public void test_registerUser_Forbidden() throws UserExistsException, OperationForbiddenException {
        initializeAuthenticatedUser();

        RegisterUserRequestDto registerUserRequestDto = new RegisterUserRequestDto();
        registerUserRequestDto.setUsername(USERNAME);
        userService.registerUser(registerUserRequestDto);
    }

    @Test(expected = OperationForbiddenException.class)
    public void test_getAllUsers_Unauthenticated() throws OperationForbiddenException {
        initializeUnauthenticatedUser();

        userService.getAllUsers();
    }

    @Test(expected = OperationForbiddenException.class)
    public void test_getAllUsers_WithUser() throws OperationForbiddenException {
        initializeAuthenticatedUser();

        userService.getAllUsers();
    }

    @Test
    public void test_getAllUsers_WithUserManager() throws OperationForbiddenException {
        initializeAuthenticatedUserManager();
        initializeUsersList();

        List<UserDto> users = userService.getAllUsers();
        assertEquals(1, users.size());
    }

    @Test
    public void test_getAllUsers_WithAdmin() throws OperationForbiddenException {
        initializeUsersList();

        List<UserDto> users = userService.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test(expected = OperationForbiddenException.class)
    public void test_getUser_Unauthenticated() throws OperationForbiddenException, ResourceNotFoundException {
        initializeUnauthenticatedUser();
        UUID uid = UUID.randomUUID();

        userService.getUser(uid);
    }

    @Test
    public void test_getUser_WithAdmin() throws OperationForbiddenException, ResourceNotFoundException {
        User user = new User();
        UUID uid = user.getUid();

        PowerMockito.when(userRepository.findById(uid)).thenReturn(Optional.of(user));
        UserDto userDto = userService.getUser(uid);

        assertEquals(uid, userDto.getUid());
    }

    @Test
    public void test_getUser_WithUserManager() throws OperationForbiddenException, ResourceNotFoundException {
        initializeAuthenticatedUserManager();
        User user = new User();
        Role userRole = new Role(RoleType.user);
        user.setRole(userRole);
        UUID uid = user.getUid();

        PowerMockito.when(userRepository.findById(uid)).thenReturn(Optional.of(user));
        UserDto userDto = userService.getUser(uid);

        assertEquals(uid, userDto.getUid());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void test_getUser_InvalidId() throws OperationForbiddenException, ResourceNotFoundException {
        UUID uid = UUID.randomUUID();

        PowerMockito.when(userRepository.findById(uid)).thenReturn(Optional.empty());
        userService.getUser(uid);
    }

    @Test(expected = OperationForbiddenException.class)
    public void test_updateUser_Unauthenticated()
            throws OperationForbiddenException, ResourceNotFoundException, InvalidResourceException {
        initializeUnauthenticatedUser();
        UUID uid = UUID.randomUUID();
        UpdateUserRequestDto requestDto = new UpdateUserRequestDto();
        userService.updateUser(uid, requestDto);
    }

    @Test
    public void test_updateUser_WithAdmin()
            throws OperationForbiddenException, ResourceNotFoundException, InvalidResourceException {
        UUID uid = admin.getUid();
        UpdateUserRequestDto requestDto = new UpdateUserRequestDto();
        requestDto.setRole(RoleType.admin);
        PowerMockito.when(userRepository.findById(uid)).thenReturn(Optional.of(admin));
        UserDto userDto = userService.updateUser(uid, requestDto);

        assertEquals(uid, userDto.getUid());
    }

    @Test
    public void test_updateUser_WithUserManager()
            throws OperationForbiddenException, ResourceNotFoundException, InvalidResourceException {
        User userManager = initializeAuthenticatedUserManager();
        UUID uid = userManager.getUid();
        UpdateUserRequestDto requestDto = new UpdateUserRequestDto();
        requestDto.setRole(RoleType.user);
        PowerMockito.when(userRepository.findById(uid)).thenReturn(Optional.of(userManager));
        PowerMockito.when(userRepository.save(any(User.class))).thenReturn(userManager);
        UserDto userDto = userService.updateUser(uid, requestDto);

        assertEquals(uid, userDto.getUid());
    }

    @Test(expected = OperationForbiddenException.class)
    public void test_updateUser_Administrator_Forbidden()
            throws OperationForbiddenException, ResourceNotFoundException, InvalidResourceException {
        User administrator = new User();
        administrator.setUsername(ChronosConstants.ADMINISTRATOR);
        UUID uid = administrator.getUid();
        UpdateUserRequestDto requestDto = new UpdateUserRequestDto();
        requestDto.setRole(RoleType.admin);
        PowerMockito.when(userRepository.findById(uid)).thenReturn(Optional.of(administrator));
        userService.updateUser(uid, requestDto);
    }

    @Test(expected = OperationForbiddenException.class)
    public void test_updateUser_WithUserManager_Forbidden()
            throws OperationForbiddenException, ResourceNotFoundException, InvalidResourceException {
        initializeAuthenticatedUserManager();
        UUID uid = admin.getUid();
        UpdateUserRequestDto requestDto = new UpdateUserRequestDto();
        requestDto.setRole(RoleType.admin);
        PowerMockito.when(userRepository.findById(uid)).thenReturn(Optional.of(admin));
        userService.updateUser(uid, requestDto);
    }

    @Test(expected = OperationForbiddenException.class)
    public void test_updateUser_WithUser_Forbidden()
            throws OperationForbiddenException, ResourceNotFoundException, InvalidResourceException {
        User user = initializeAuthenticatedUser();
        UUID uid = user.getUid();
        UpdateUserRequestDto requestDto = new UpdateUserRequestDto();
        requestDto.setRole(RoleType.admin);
        PowerMockito.when(userRepository.findById(uid)).thenReturn(Optional.of(admin));
        userService.updateUser(uid, requestDto);
    }

    @Test(expected = InvalidResourceException.class)
    public void test_updateUser_InvalidRequest()
            throws OperationForbiddenException, ResourceNotFoundException, InvalidResourceException {
        User userManager = initializeAuthenticatedUserManager();
        UUID uid = userManager.getUid();
        UpdateUserRequestDto requestDto = new UpdateUserRequestDto();
        PowerMockito.when(userRepository.findById(uid)).thenReturn(Optional.of(userManager));
        userService.updateUser(uid, requestDto);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void test_updateUser_InvalidId()
            throws OperationForbiddenException, ResourceNotFoundException, InvalidResourceException {
        User userManager = initializeAuthenticatedUserManager();
        UUID uid = userManager.getUid();
        UpdateUserRequestDto requestDto = new UpdateUserRequestDto();
        PowerMockito.when(userRepository.findById(uid)).thenReturn(Optional.empty());
        userService.updateUser(uid, requestDto);
    }

    @Test(expected = OperationForbiddenException.class)
    public void test_updateUserPassword_Unauthenticated()
            throws OperationForbiddenException, ResourceNotFoundException {
        initializeUnauthenticatedUser();
        UUID uid = UUID.randomUUID();
        UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto();
        userService.updateUserPassword(uid, updatePasswordDto);
    }

    @Test
    public void test_updateUserPassword() throws OperationForbiddenException, ResourceNotFoundException {
        UUID uid = admin.getUid();
        UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto();
        PowerMockito.when(userRepository.findById(uid)).thenReturn(Optional.of(admin));
        PowerMockito.when(passwordEncoder.matches(any(), any())).thenReturn(true);
        userService.updateUserPassword(uid, updatePasswordDto);

        verify(userRepository, times(1)).save(any());
    }

    @Test(expected = OperationForbiddenException.class)
    public void test_updateUser_InvalidPassword() throws OperationForbiddenException, ResourceNotFoundException {
        UUID uid = admin.getUid();
        UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto();
        PowerMockito.when(userRepository.findById(uid)).thenReturn(Optional.of(admin));
        PowerMockito.when(passwordEncoder.matches(any(), any())).thenReturn(false);
        userService.updateUserPassword(uid, updatePasswordDto);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void test_updateUserPassword_InvalidId() throws OperationForbiddenException, ResourceNotFoundException {
        UUID uid = UUID.randomUUID();
        UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto();
        PowerMockito.when(userRepository.findById(uid)).thenReturn(Optional.empty());
        userService.updateUserPassword(uid, updatePasswordDto);
    }

    @Test(expected = OperationForbiddenException.class)
    public void test_deleteUser_Unauthenticated() throws OperationForbiddenException, ResourceNotFoundException {
        initializeUnauthenticatedUser();
        UUID uid = UUID.randomUUID();
        userService.deleteUser(uid);
    }

    @Test
    public void test_deleteUser_WithAdmin() throws OperationForbiddenException, ResourceNotFoundException {
        UUID uid = admin.getUid();
        PowerMockito.when(userRepository.findById(uid)).thenReturn(Optional.of(admin));
        userService.deleteUser(uid);

        verify(userRepository, times(1)).delete(any());
    }

    @Test
    public void test_deleteUser_WithUserManager() throws OperationForbiddenException, ResourceNotFoundException {
        User userManager = initializeAuthenticatedUserManager();
        UUID uid = userManager.getUid();
        PowerMockito.when(userRepository.findById(uid)).thenReturn(Optional.of(userManager));
        userService.deleteUser(uid);

        verify(userRepository, times(1)).delete(any());
    }

    @Test(expected = OperationForbiddenException.class)
    public void test_deleteUser_Administrator_Forbidden() throws OperationForbiddenException, ResourceNotFoundException {
        User administrator = new User();
        administrator.setUsername(ChronosConstants.ADMINISTRATOR);
        UUID uid = administrator.getUid();
        PowerMockito.when(userRepository.findById(uid)).thenReturn(Optional.of(administrator));
        userService.deleteUser(uid);
        verify(userRepository, times(1)).delete(any());
    }

    @Test(expected = OperationForbiddenException.class)
    public void test_deleteUser_WithUserManager_Forbidden() throws OperationForbiddenException, ResourceNotFoundException {
        initializeAuthenticatedUserManager();
        UUID uid = admin.getUid();
        PowerMockito.when(userRepository.findById(uid)).thenReturn(Optional.of(admin));
        userService.deleteUser(uid);
    }

    @Test(expected = OperationForbiddenException.class)
    public void test_deleteUser_WithUser_Forbidden() throws OperationForbiddenException, ResourceNotFoundException {
        User user = initializeAuthenticatedUser();
        UUID uid = user.getUid();
        PowerMockito.when(userRepository.findById(uid)).thenReturn(Optional.of(admin));
        userService.deleteUser(uid);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void test_deleteUser_InvalidId() throws OperationForbiddenException, ResourceNotFoundException {
        User userManager = initializeAuthenticatedUserManager();
        UUID uid = userManager.getUid();
        PowerMockito.when(userRepository.findById(uid)).thenReturn(Optional.empty());
        userService.deleteUser(uid);
    }

    private void initializeUsersList() {
        List<User> users = new ArrayList<>();
        users.add(admin);

        User user = new User();
        Role userRole = new Role(RoleType.user);
        user.setRole(userRole);
        users.add(user);
        PowerMockito.when(userRepository.findAll()).thenReturn(users);
    }

    private void initializeUnauthenticatedUser() {
        SecurityContext securityContext = PowerMockito.mock(SecurityContext.class);
        Authentication authentication = PowerMockito.mock(Authentication.class);
        PowerMockito.when(authentication.getPrincipal()).thenReturn(null);
        PowerMockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        PowerMockito.when(SecurityContextHolder.getContext()).thenReturn(securityContext);
    }

    private void initializeAuthenticatedAdmin() {
        role = new Role();
        role.setType(RoleType.admin);
        admin = new User();
        admin.setUsername(USERNAME);
        admin.setRole(role);

        AuthUserDetails authUserDetails = new AuthUserDetails(admin);
        SecurityContext securityContext = PowerMockito.mock(SecurityContext.class);
        Authentication authentication = PowerMockito.mock(Authentication.class);
        PowerMockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
        PowerMockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        PowerMockito.when(SecurityContextHolder.getContext()).thenReturn(securityContext);
    }

    private User initializeAuthenticatedUserManager() {
        Role userManagerRole = new Role();
        userManagerRole.setType(RoleType.user_manager);
        User userManager = new User();
        userManager.setUsername("user_manager");
        userManager.setRole(userManagerRole);

        AuthUserDetails authUserDetails = new AuthUserDetails(userManager);
        SecurityContext securityContext = PowerMockito.mock(SecurityContext.class);
        Authentication authentication = PowerMockito.mock(Authentication.class);
        PowerMockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
        PowerMockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        PowerMockito.when(SecurityContextHolder.getContext()).thenReturn(securityContext);

        return userManager;
    }

    private User initializeAuthenticatedUser() {
        Role userRole = new Role();
        userRole.setType(RoleType.user);
        User user = new User();
        user.setUsername("user");
        user.setRole(userRole);

        AuthUserDetails authUserDetails = new AuthUserDetails(user);
        SecurityContext securityContext = PowerMockito.mock(SecurityContext.class);
        Authentication authentication = PowerMockito.mock(Authentication.class);
        PowerMockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
        PowerMockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        PowerMockito.when(SecurityContextHolder.getContext()).thenReturn(securityContext);

        return user;
    }
}
