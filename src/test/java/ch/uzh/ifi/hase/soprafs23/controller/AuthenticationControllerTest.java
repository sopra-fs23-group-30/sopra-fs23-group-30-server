package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.constant.ProfileStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Profile;
import ch.uzh.ifi.hase.soprafs23.rest.dto.RegisterProfileDTO;
import ch.uzh.ifi.hase.soprafs23.service.ProfileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(AuthenticationController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileService profileService;

    @Test
    public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
        // given
        Profile profile = new Profile();
        profile.setFirstname("Firstname");
        profile.setLastname("Lastname");
        profile.setStatus(ProfileStatus.OFFLINE);

        List<Profile> allProfiles = Collections.singletonList(profile);

        // this mocks the UserService -> we define above what the userService should
        // return when getUsers() is called
        given(profileService.getUsers()).willReturn(allProfiles);

        // when
        MockHttpServletRequestBuilder getRequest = get("/profiles").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstname", is(profile.getFirstname())))
                .andExpect(jsonPath("$[0].lastname", is(profile.getLastname())))
                .andExpect(jsonPath("$[0].status", is(profile.getStatus().toString())));
    }

    @Test
    public void createProfile_validInput_userCreated() throws Exception {
        // given
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setFirstname("Test");
        profile.setLastname("Profile");
        profile.setToken("1");
        profile.setEmail("test.profile@example.com");
        profile.setPhoneNumber("0781234567");
        profile.setPassword("OneTwoThreeFour");
        profile.setSearcher(true);
        profile.setStatus(ProfileStatus.ONLINE);

        RegisterProfileDTO registerProfileDTO = new RegisterProfileDTO();
        registerProfileDTO.setFirstname("Test");
        registerProfileDTO.setLastname("Profile");
        registerProfileDTO.setEmail("test.profile@example.com");
        registerProfileDTO.setPhoneNumber("0781234567");
        registerProfileDTO.setPassword("OneTwoThreeFour");
        registerProfileDTO.setSearcher(true);

        given(profileService.createUser(Mockito.any())).willReturn(profile);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(registerProfileDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token", is(profile.getToken())));
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