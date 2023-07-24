//package com.sparta.pinterest_clone;
//
//import com.sparta.pinterest_clone.security.UserDetailsImpl;
//import com.sparta.pinterest_clone.user.dto.UpdateProfileRequestDto;
//import com.sparta.pinterest_clone.user.entity.User;
//import com.sparta.pinterest_clone.user.repository.UserRepository;
//import com.sparta.pinterest_clone.user.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//
//@RequiredArgsConstructor
//class UserServiceTest {
//    @Mock
//    UserRepository userRepository = Mockito.mock(UserRepository.class);
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @InjectMocks
//    private UserService userService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//    @Test
//    void updateProfile_ValidRequestDto_ShouldUpdateProfile() {
//
//        // Given
//        Long userId = 1L;
//        String email = "test@example.com";
//        String password = "testPassword";
//        String birthday = "950901";
//        String firstname = "jihun";
//        String lastname = "kim";
//        String introduction = "자기소개";
//        String myUrl = "https://example.com";
//        String username = "kjh0901";
//
//        // Create a user with mock data
//        User user = new User(email, password, birthday);
//        user.setUserId(userId);
//        user.setFirstName(firstname);
//        user.setLastName(lastname);
//        user.setIntroduction(introduction);
//        user.setMyUrl(myUrl);
//        user.setUsername(username);
//
//        // Mock the userRepository.findById() method
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//
//        // Request DTO with updated profile information
//        UpdateProfileRequestDto requestDto = new UpdateProfileRequestDto();
//        requestDto.setFirstname("abcd");
//        requestDto.setLastname("abcd");
//        requestDto.setIntroduction("수정수정");
//        requestDto.setMyUrl("https://new-example.com");
//        requestDto.setUsername("kim123");
//
//        // UserDetailsImpl with the mock user
//        UserDetailsImpl userDetails = new UserDetailsImpl(user);
//
//        userService.updateProfile(userDetails, requestDto);
//
//        // Then
//        // Verify that userRepository.save() is called to update the user profile
//        verify(userRepository, times(1)).save(any(User.class));
//
//        // Assert that the user's profile has been updated with the new information
//        assert user.getFirstName().equals("Jane") : "First name is not updated";
//        assert user.getLastName().equals("Smith") : "Last name is not updated";
//        assert user.getIntroduction().equals("Hello, I'm Jane!") : "Introduction is not updated";
//        assert user.getMyUrl().equals("https://new-example.com") : "MyUrl is not updated";
//        assert user.getUsername().equals("jane.smith") : "Username is not updated";
//    }
//}
