package ch.uzh.ifi.hase.soprafs23.controller;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.uzh.ifi.hase.soprafs23.config.JwtAuthenticationEntryPoint;
import ch.uzh.ifi.hase.soprafs23.config.JwtRequest;
import ch.uzh.ifi.hase.soprafs23.config.JwtRequestFilter;
import ch.uzh.ifi.hase.soprafs23.config.JwtTokenUtil;
import ch.uzh.ifi.hase.soprafs23.entity.ProfileEntity;
import ch.uzh.ifi.hase.soprafs23.repository.ProfileRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.profile.RegisterPostDTO;
import ch.uzh.ifi.hase.soprafs23.service.ListingService;
import ch.uzh.ifi.hase.soprafs23.service.ProfileService;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ContextConfiguration(classes = AuthenticationController.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private ProfileRepository profileRepository;

    @MockBean
    private ProfileService profileService;

    @MockBean
    private ListingService listingService;

    @MockBean
    private JwtRequestFilter jwtRequestFilter;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private AuthenticationController authenticationController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AuthenticationController(profileService, authenticationManager, jwtTokenUtil))
                .build();
    }

    @Test
    void givenEmailNotExisting_whenRegisterUser_thenReturnCreated() throws Exception {
        String email = "test.example@gmail.com";
        String password = "OneTwoThreeFour";

        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setFirstname("Test");
        profileEntity.setLastname("Profile");
        profileEntity.setEmail(email);
        profileEntity.setPhoneNumber("0781234567");
        profileEntity.setPassword(password);
        profileEntity.setIsSearcher(true);

        RegisterPostDTO registerPostDTO = new RegisterPostDTO();
        registerPostDTO.setFirstname("Test");
        registerPostDTO.setLastname("Profile");
        registerPostDTO.setEmail(email);
        registerPostDTO.setPhoneNumber("0781234567");
        registerPostDTO.setPassword(password);
        registerPostDTO.setIsSearcher(true);

        given(profileService.createUser(Mockito.any())).willReturn(profileEntity);

        MockHttpServletRequestBuilder postRequest = post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(registerPostDTO));

        mockMvc.perform(postRequest).andExpect(status().isCreated());
    }

    @Test
    void givenEmailExisting_whenRegisterUser_thenReturnBadRequest() throws Exception {
        String email = "test.example@gmail.com";
        String password = "OneTwoThreeFour";

        RegisterPostDTO registerPostDTO = new RegisterPostDTO();
        registerPostDTO.setFirstname("Test");
        registerPostDTO.setLastname("Profile");
        registerPostDTO.setEmail(email);
        registerPostDTO.setPhoneNumber("0781234567");
        registerPostDTO.setPassword(password);
        registerPostDTO.setIsSearcher(true);

        given(profileService.createUser(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "The provided e-mail is not unique. Therefore, the profile could not be registered!"));

        MockHttpServletRequestBuilder postRequest = post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(registerPostDTO));

        mockMvc.perform(postRequest).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void givenEmailExisting_whenLoginUser_thenLoginSuccessful() throws Exception {
        String email = "test.example@gmail.com";
        String password = "OneTwoThreeFour";
        String toGenerateToken = "testtoken";

        JwtRequest authenticationRequest = new JwtRequest();
        authenticationRequest.setEmail(email);
        authenticationRequest.setPassword(password);

        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setEmail(email);
        profileEntity.setPassword(password);

        given(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword())))
                .willReturn(Mockito.mock(Authentication.class));

        given(profileService.getProfileBySigninCredentials(authenticationRequest)).willReturn(profileEntity);

        given(jwtTokenUtil.generateToken(Mockito.any())).willReturn(toGenerateToken);

        MockHttpServletRequestBuilder postRequest = post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(authenticationRequest));

        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(toGenerateToken)));
        // .andExpect(jsonPath("$.token").value(toGenerateToken));
    }

    @Test
    void givenEmailDisabled_whenLoginUser_thenThrowDisabledException() throws Exception {
        String email = "test.example@gmail.com";
        String password = "OneTwoThreeFour";

        JwtRequest authenticationRequest = new JwtRequest();
        authenticationRequest.setEmail(email);
        authenticationRequest.setPassword(password);

        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setEmail(email);
        profileEntity.setPassword(password);

        given(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword())))
                .willThrow(new DisabledException(""));

        MockHttpServletRequestBuilder postRequest = post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(authenticationRequest));

        try {
            mockMvc.perform(postRequest);
            assertTrue(false);
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("USER_DISABLED"));
        }
    }

    @Test
    void givenEmailAndPasswordInvalid_whenLoginUser_thenThrowUnauthenticatedException() throws Exception {
        String email = "test.example@gmail.com";
        String password = "OneTwoThreeFour";

        JwtRequest authenticationRequest = new JwtRequest();
        authenticationRequest.setEmail(email);
        authenticationRequest.setPassword(password);

        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setEmail(email);
        profileEntity.setPassword(password);

        given(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword())))
                .willThrow(new BadCredentialsException(""));

        MockHttpServletRequestBuilder postRequest = post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(authenticationRequest));

        try {
            mockMvc.perform(postRequest);
            assertTrue(false);
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("INVALID_CREDENTIALS"));
        }
    }

    /**
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }
}