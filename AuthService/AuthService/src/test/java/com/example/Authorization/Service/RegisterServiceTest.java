//package com.example.Authorization.Service;
//
//import com.example.Authorization.DTO.RegisterRequest;
//import com.example.Authorization.Entity.Role;
//import com.example.Authorization.Entity.User;
//import com.example.Authorization.Repository.RoleRepository;
//import com.example.Authorization.Repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Tag;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Optional;
//import java.util.Set;
//
//import static com.example.Authorization.Enum.RoleName.ROLE_USER;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//@Tag("unit")
//class RegisterServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private RoleRepository roleRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @InjectMocks
//    private RegisterService registerService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void register_WhenUsernameAndEmailAreUnique_ShouldSaveUserWithEncodedPasswordAndDefaultRole() {
//        // Given
//        RegisterRequest request = new RegisterRequest();
//        request.setUsername("newuser");
//        request.setEmail("newuser@example.com");
//        request.setPassword("rawPassword");
//
//        Role userRole = new Role();
//        userRole.setName(ROLE_USER);
//
//        when(userRepository.existsByUsername("newuser")).thenReturn(false);
//        when(userRepository.existsByEmail("newuser@example.com")).thenReturn(false);
//        when(roleRepository.findByName(ROLE_USER)).thenReturn(Optional.of(userRole));
//        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
//
//        // When
//        registerService.register(request);
//
//        // Then
//        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
//        verify(userRepository).save(userCaptor.capture());
//
//        User savedUser = userCaptor.getValue();
//        assertEquals("newuser", savedUser.getUsername());
//        assertEquals("newuser@example.com", savedUser.getEmail());
//        assertEquals("encodedPassword", savedUser.getPassword());
//        assertTrue(savedUser.isEnabled());
//        assertEquals(Set.of(userRole), savedUser.getRoles());
//    }
//
//    @Test
//    void register_WhenUsernameExists_ShouldThrowException() {
//        // Given
//        RegisterRequest request = new RegisterRequest();
//        request.setUsername("existinguser");
//        when(userRepository.existsByUsername("existinguser")).thenReturn(true);
//
//        // Expect
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
//                registerService.register(request));
//        assertEquals("Username is already taken", exception.getMessage());
//    }
//
//    @Test
//    void register_WhenEmailExists_ShouldThrowException() {
//        // Given
//        RegisterRequest request = new RegisterRequest();
//        request.setUsername("user");
//        request.setEmail("existing@example.com");
//
//        when(userRepository.existsByUsername("user")).thenReturn(false);
//        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);
//
//        // Expect
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
//                registerService.register(request));
//        assertEquals("Email is already in use", exception.getMessage());
//    }
//
//    @Test
//    void register_WhenDefaultRoleMissing_ShouldThrowException() {
//        // Given
//        RegisterRequest request = new RegisterRequest();
//        request.setUsername("user");
//        request.setEmail("user@example.com");
//        request.setPassword("123");
//
//        when(userRepository.existsByUsername("user")).thenReturn(false);
//        when(userRepository.existsByEmail("user@example.com")).thenReturn(false);
//        when(roleRepository.findByName(ROLE_USER)).thenReturn(Optional.empty());
//
//        // Expect
//        RuntimeException exception = assertThrows(RuntimeException.class, () ->
//                registerService.register(request));
//        assertEquals("Default role not found", exception.getMessage());
//    }
//}
