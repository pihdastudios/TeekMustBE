package com.binar.teekmustbe;

import com.binar.teekmustbe.controller.SignUpLoginController;
import com.binar.teekmustbe.controller.UserController;
import com.binar.teekmustbe.controller.UserProfileController;
import com.binar.teekmustbe.dto.ProfileDto;
import com.binar.teekmustbe.dto.UserDto;
import com.binar.teekmustbe.dto.UserSignupDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class ProfileTest {
    private static final Logger logger = LoggerFactory.getLogger(ProfileTest.class);

    @Autowired
    private SignUpLoginController signUpLoginController;
    @Autowired
    private UserProfileController userProfileController;
    @Autowired
    private UserController userController;

    @Test
    public void profileTest() throws IOException {
        signUpLoginController.signUp(new UserSignupDto()
                        .setUsername("testuser")
                        .setPassword("password")
                        .setAddress("Test Ave.")
                        .setEmail("test@test.com")
                        .setRoles(Set.of("buyer"))
                        .setNumber("1234567"),
                new MockMultipartFile("test_profile_photo",
                        "test_profile_photo.jpg",
                        "application/octet-stream",
                        new ClassPathResource("img/test_profile_photo.jpg")
                                .getInputStream()));
        userProfileController.update(new ProfileDto(((Optional<UserDto>) Objects.requireNonNull(userController.findByUsername("testuser").getBody())).get())
                .setAddress("tesAddress")
                .setNumber("7654321"),
                new MockMultipartFile("test_profile_photo",
                        "test_profile_photo.jpg",
                        "application/octet-stream",
                        new ClassPathResource("img/test_profile_photo.jpg")
                                .getInputStream()));
        var response = userController.findByUsername("testuser");
        assertEquals("tesAddress", ((Optional<UserDto>) Objects.requireNonNull(response.getBody())).get().getAddress());
        assertEquals("7654321", ((Optional<UserDto>) Objects.requireNonNull(response.getBody())).get().getNumber());

    }
}
