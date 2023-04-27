package ch.uzh.ifi.hase.soprafs23.controller;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.uzh.ifi.hase.soprafs23.constant.ApplicationState;
import ch.uzh.ifi.hase.soprafs23.entity.ApplicationEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ListingEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ProfileEntity;
import ch.uzh.ifi.hase.soprafs23.rest.dto.application.ApplicationPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.application.ApplicationPutDTO;
import ch.uzh.ifi.hase.soprafs23.service.ApplicationService;
import ch.uzh.ifi.hase.soprafs23.service.ListingService;
import ch.uzh.ifi.hase.soprafs23.service.ProfileService;

@WebMvcTest(ApplicationsController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ContextConfiguration(classes = ApplicationsController.class)
public class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationService applicationService;

    @MockBean
    private ProfileService profileService;

    @MockBean
    private ListingService listingService;
    
    private ProfileEntity profileEntity;
    private ListingEntity listingEntity;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ApplicationsController(applicationService, profileService, listingService))
                .build();

        ProfileEntity lister = new ProfileEntity();
        lister.setId(UUID.randomUUID());

        listingEntity = new ListingEntity();
        listingEntity.setId(UUID.randomUUID());
        listingEntity.setTitle("Nice Listing");
        listingEntity.setDescription("Nice Listing with description");
        listingEntity.setStreetName("Kronestutz");
        listingEntity.setStreetNumber("1");
        listingEntity.setZipCode(4500);
        listingEntity.setCityName("Solothurn");
        listingEntity.setPricePerMonth(500);
        listingEntity.setPerfectFlatmateDescription("A person, preferably alive");
        listingEntity.setLister(lister);
        listingEntity.setImagesJson("");
        listingEntity.setCreationDate(LocalDateTime.now());

        String email = "test.example@gmail.com";
        String password = "OneTwoThreeFour";

        profileEntity = new ProfileEntity();
        profileEntity.setId(UUID.randomUUID());
        profileEntity.setFirstname("Test");
        profileEntity.setLastname("Profile");
        profileEntity.setEmail(email);
        profileEntity.setPhoneNumber("0781234567");
        profileEntity.setPassword(password);
        profileEntity.setIsSearcher(true);
    }

    @Test
    void createApplication_validInput_success() throws Exception {
        ApplicationPostDTO applicationPostDTO = new ApplicationPostDTO();
        applicationPostDTO.setListingId(listingEntity.getId());
        applicationPostDTO.setApplicantId(profileEntity.getId());

        Mockito.when(profileService.getProfileById(Mockito.any())).thenReturn(profileEntity);
        Mockito.when(listingService.getListingById(Mockito.any())).thenReturn(listingEntity);

        
        MockHttpServletRequestBuilder postRequest = post("/applications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(applicationPostDTO));


        mockMvc.perform(postRequest).andExpect(status().isCreated());
    }

    @Test
    void updateApplication_validInput_success() throws Exception {
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setId(UUID.randomUUID());
        applicationEntity.setApplicant(profileEntity);
        applicationEntity.setListing(listingEntity);
        applicationEntity.setCreationDate(LocalDateTime.now());
        applicationEntity.setState(ApplicationState.PENDING);

        ApplicationPutDTO applicationPutDTO = new ApplicationPutDTO();
        applicationPutDTO.setNewState(ApplicationState.DECLINED);

        Mockito.when(applicationService.getApplicationById(Mockito.any())).thenReturn(applicationEntity);

        
        MockHttpServletRequestBuilder putRequest = put("/applications/" + applicationEntity.getId().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(applicationPutDTO));


        mockMvc.perform(putRequest).andExpect(status().isNoContent());
    }

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }
}
