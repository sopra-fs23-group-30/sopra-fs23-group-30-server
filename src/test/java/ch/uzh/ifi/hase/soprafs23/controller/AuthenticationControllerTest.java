package ch.uzh.ifi.hase.soprafs23.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
import ch.uzh.ifi.hase.soprafs23.rest.dto.Profile.RegisterPostDTO;
import ch.uzh.ifi.hase.soprafs23.service.ListingService;
import ch.uzh.ifi.hase.soprafs23.service.ProfileService;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
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
    public void setup() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(new AuthenticationController(profileService, authenticationManager, jwtTokenUtil))
            .addFilter(jwtRequestFilter, "")
            .build();    
        }

    @Test
    void givenEmailNotExisting_whenRegisterUser_thenReturnCreated() throws Exception {
        String email = "test.example@gmail.com";
        String password = "OneTwoThreeFour";

        // given
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

        // when
        MockHttpServletRequestBuilder postRequest = post("/registration")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(registerPostDTO));

        // then
        mockMvc.perform(postRequest).andExpect(status().isCreated());
    }

    @Test
    void givenEmailExisting_whenRegisterUser_thenReturnBadRequest() throws Exception {
        String email = "test.example@gmail.com";
        String password = "OneTwoThreeFour";

        // given
        RegisterPostDTO registerPostDTO = new RegisterPostDTO();
        registerPostDTO.setFirstname("Test");
        registerPostDTO.setLastname("Profile");
        registerPostDTO.setEmail(email);
        registerPostDTO.setPhoneNumber("0781234567");
        registerPostDTO.setPassword(password);
        registerPostDTO.setIsSearcher(true);

        given(profileService.createUser(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "The provided e-mail is not unique. Therefore, the profile could not be registered!"));

        // when
        MockHttpServletRequestBuilder postRequest = post("/registration")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(registerPostDTO));

        // then
        mockMvc.perform(postRequest).andExpect(status().isBadRequest());
    }

    @Test
    void givenEmailExisting_whenLoginUser_thenLoginSuccessful() throws Exception {
        String email = "test.example@gmail.com";
        String password = "OneTwoThreeFour";
        String toGenerateToken = "testtoken";

        // given
        JwtRequest authenticationRequest = new JwtRequest();
        authenticationRequest.setEmail(email);
        authenticationRequest.setPassword(password);

        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setEmail(email);
        profileEntity.setPassword(password);

        given(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())))
            .willReturn(Mockito.mock(Authentication.class));

        given(profileService.getProfileBySigninCredentials(authenticationRequest)).willReturn(profileEntity);

        given(jwtTokenUtil.generateToken(Mockito.any())).willReturn(toGenerateToken); 

        // when
        MockHttpServletRequestBuilder postRequest = post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(authenticationRequest));


        // then
        mockMvc.perform(postRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token", is(toGenerateToken)));
    }

    // @Test
    // public void givenEmailDisabled_whenLoginUser_thenThrowDisabledException() throws Exception {
    //     String email = "test.example@gmail.com";
    //     String password = "OneTwoThreeFour";

    //     // given
    //     JwtRequest authenticationRequest = new JwtRequest();
    //     authenticationRequest.setEmail(email);
    //     authenticationRequest.setPassword(password);

    //     ProfileEntity profileEntity = new ProfileEntity();
    //     profileEntity.setEmail(email);
    //     profileEntity.setPassword(password);

    //     given(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())))
    //         .willThrow(new DisabledException(""));

    //     // when
    //     MockHttpServletRequestBuilder postRequest = post("/login")
    //         .contentType(MediaType.APPLICATION_JSON)
    //         .content(asJsonString(authenticationRequest));


    //     // then
    //     mockMvc.perform(postRequest)
    //         .andExpect(result -> assertTrue(result.getResolvedException() instanceof Exception));
    // }

    // @Test
    // public void invalidRequest() throws Exception {
    //     // given
    //     ListingPostDTO listingPostDTO = new ListingPostDTO(); 
    //     listingPostDTO.setTitle("Schöns Listing 56");
    //     listingPostDTO.setDescription("testdescription");
    //     listingPostDTO.setStreetName("teststreet");
    //     listingPostDTO.setStreetNumber("10b");
    //     listingPostDTO.setZipCode(4500);
    //     listingPostDTO.setCityName("Solothurn");
    //     listingPostDTO.setPricePerMonth(620);
    //     listingPostDTO.setPerfectFlatmateDescription("My perfect flatmate is black");
    //     listingPostDTO.setListerId(new UUID(0, 0));

    //     ListingEntity listingEntity = new ListingEntity();
    //     listingEntity.setTitle("Schöns Listing 56");
    //     listingEntity.setDescription("testdescription");
    //     listingEntity.setStreetName("teststreet");
    //     listingEntity.setStreetNumber("10b");
    //     listingEntity.setZipCode(4500);
    //     listingEntity.setCityName("Solothurn");
    //     listingEntity.setPricePerMonth(620);
    //     listingEntity.setPerfectFlatmateDescription("My perfect flatmate is black");
    //     listingEntity.setLister(new ProfileEntity());

    //     given(listingService.createListing(Mockito.any())).willReturn(listingEntity);

    //     // when
    //     MockHttpServletRequestBuilder postRequest = post("/listings")
    //         .contentType(MediaType.APPLICATION_JSON)
    //         .content(asJsonString(listingPostDTO));

    //     // then
    //     mockMvc.perform(postRequest).andExpect(status().isCreated());
    // }

    // @Test
    // public void assertTrue_whenTrue() throws Exception {
    //     // given
    //     assertTrue(true, "mes");
    // }

    // @Test
    // public void createProfile_validInput_userCreated() throws Exception {
    //     // given
    //     Profile profile = new Profile();
    //     profile.setId(1L);
    //     profile.setFirstname("Test");
    //     profile.setLastname("Profile");
    //     profile.setToken("1");
    //     profile.setEmail("test.profile@example.com");
    //     profile.setPhoneNumber("0781234567");
    //     profile.setPassword("OneTwoThreeFour");
    //     profile.setSearcher(true);
    //     profile.setStatus(ProfileStatus.ONLINE);

    //     RegisterProfileDTO registerProfileDTO = new RegisterProfileDTO();
    //     registerProfileDTO.setFirstname("Test");
    //     registerProfileDTO.setLastname("Profile");
    //     registerProfileDTO.setEmail("test.profile@example.com");
    //     registerProfileDTO.setPhoneNumber("0781234567");
    //     registerProfileDTO.setPassword("OneTwoThreeFour");
    //     registerProfileDTO.setSearcher(true);

    //     given(profileService.createUser(Mockito.any())).willReturn(profile);

    //     // when/then -> do the request + validate the result
    //     MockHttpServletRequestBuilder postRequest = post("/registration")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(asJsonString(registerProfileDTO));

    //     // then
    //     mockMvc.perform(postRequest)
    //             .andExpect(status().isCreated())
    //             .andExpect(jsonPath("$.token", is(profile.getToken())));
    // }

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