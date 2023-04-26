package ch.uzh.ifi.hase.soprafs23.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ch.uzh.ifi.hase.soprafs23.config.JwtAuthenticationEntryPoint;
import ch.uzh.ifi.hase.soprafs23.config.JwtRequestFilter;
import ch.uzh.ifi.hase.soprafs23.config.JwtTokenUtil;
import ch.uzh.ifi.hase.soprafs23.entity.ApplicationEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ListingEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ProfileEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ProfileLifespanEntity;
import ch.uzh.ifi.hase.soprafs23.repository.ProfileRepository;
import ch.uzh.ifi.hase.soprafs23.service.ApplicationService;
import ch.uzh.ifi.hase.soprafs23.service.BlobUploaderService;
import ch.uzh.ifi.hase.soprafs23.service.ListingService;
import ch.uzh.ifi.hase.soprafs23.service.ProfileLifespanService;
import ch.uzh.ifi.hase.soprafs23.service.ProfileService;

@WebMvcTest(ProfilesController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ContextConfiguration(classes = ProfilesController.class)
public class ProfilesControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private ProfileRepository profileRepository;

        @MockBean
        private ProfileService profileService;

        @MockBean
        private ApplicationService applicationService;

        @MockBean
        private ListingService listingService;

        @MockBean
        private ProfileLifespanService profileLifespanService;

        @MockBean
        private BlobUploaderService blobUploaderService;

        @MockBean
        private JwtRequestFilter jwtRequestFilter;

        @MockBean
        private JwtTokenUtil jwtTokenUtil;

        @MockBean
        private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

        @MockBean
        private ProfilesController profilesController;

        @BeforeEach
        void setup() {
                mockMvc = MockMvcBuilders
                                .standaloneSetup(new ProfilesController(profileService, applicationService,
                                                listingService,
                                                profileLifespanService, blobUploaderService))
                                .build();
        }

        @Test
        void getProfileByid_validInput_success() throws Exception {
                String email = "test.example@gmail.com";
                String password = "OneTwoThreeFour";

                ProfileEntity profileEntity = new ProfileEntity();
                profileEntity.setId(UUID.randomUUID());
                profileEntity.setFirstname("Test");
                profileEntity.setLastname("Profile");
                profileEntity.setEmail(email);
                profileEntity.setPhoneNumber("0781234567");
                profileEntity.setPassword(password);
                profileEntity.setIsSearcher(true);

                Mockito.when(profileService.getProfileById(Mockito.any())).thenReturn(profileEntity);

                ProfileLifespanEntity profileLifespanEntity = new ProfileLifespanEntity();
                profileLifespanEntity.setProfile(profileEntity);
                profileLifespanEntity.setIsExperience(true);
                profileLifespanEntity.setText("Work at XYZ");
                profileLifespanEntity.setFromDate(Date.valueOf("2002-12-26"));
                profileLifespanEntity.setToDate(Date.valueOf("2003-12-26"));
                List<ProfileLifespanEntity> profileLifespanEntityList = new ArrayList<>();
                profileLifespanEntityList.add(profileLifespanEntity);

                Mockito.when(profileLifespanService.getAllLifespansByProfileId(Mockito.any()))
                                .thenReturn(profileLifespanEntityList);

                MockHttpServletRequestBuilder getRequest = get("/profiles/" + profileEntity.getId().toString());

                mockMvc.perform(getRequest)

                                .andExpect(status().isOk());
                // .andExpect(jsonPath("$.firstname").value(profileEntity.getFirstname()))
                // .andExpect(jsonPath("$.lastname").value(profileEntity.getLastname()))
                // .andExpect(jsonPath("$.birthday").value(profileEntity.getBirthday()))
                // .andExpect(jsonPath("$.phoneNumber").value(profileEntity.getPhoneNumber()))
                // .andExpect(jsonPath("$.gender").value(profileEntity.getGender()))
                // .andExpect(jsonPath("$.biography").value(profileEntity.getBiography()))
                // .andExpect(jsonPath("$.futureFlatmatesDescription").value(profileEntity.getFutureFlatmatesDescription()))
                // .andExpect(jsonPath("$.lifespans[0]").value(profileLifespanEntity));

                // .andExpect(jsonPath("$.firstname").value(profileEntity.getFirstname()))
                // .andExpect(jsonPath("$.lastname").value(profileEntity.getLastname()))
                // .andExpect(jsonPath("$.birthday").value(profileEntity.getBirthday()))
                // .andExpect(jsonPath("$.phoneNumber").value(profileEntity.getPhoneNumber()))
                // .andExpect(jsonPath("$.gender").value(profileEntity.getGender()))
                // .andExpect(jsonPath("$.biography").value(profileEntity.getBiography()))
                // .andExpect(jsonPath("$.futureFlatmatesDescription").value(profileEntity.getFutureFlatmatesDescription()))
                // .andExpect(jsonPath("$.lifespans[0]").value(profileLifespanEntity));

        }

        @Test
        void getApplicationsByProfileId_validInput_success() throws Exception {
                String email = "test.example@gmail.com";
                String password = "OneTwoThreeFour";

                ProfileEntity profileEntity = new ProfileEntity();
                profileEntity.setId(UUID.randomUUID());
                profileEntity.setFirstname("Test");
                profileEntity.setLastname("Profile");
                profileEntity.setEmail(email);
                profileEntity.setPhoneNumber("0781234567");
                profileEntity.setPassword(password);
                profileEntity.setIsSearcher(true);

                ListingEntity listingEntity = new ListingEntity();
                listingEntity.setId(UUID.randomUUID());

                ApplicationEntity applicationEntity = new ApplicationEntity();
                applicationEntity.setId(UUID.randomUUID());
                applicationEntity.setListing(listingEntity);
                applicationEntity.setApplicant(profileEntity);
                applicationEntity.setCreationDate(LocalDateTime.now());

                List<ApplicationEntity> applicationEntities = new ArrayList<>();
                applicationEntities.add(applicationEntity);

                Mockito.when(applicationService.getAllApplicationsByProfileId(Mockito.any()))
                                .thenReturn(applicationEntities);

                MockHttpServletRequestBuilder getRequest = get(
                                "/profiles/" + profileEntity.getId().toString() + "/applications");

                mockMvc.perform(getRequest).andExpect(status().isOk());
        }

        @Test
        void getListingbyProfileId_validInput_success() throws Exception {
                String email = "test.example@gmail.com";
                String password = "OneTwoThreeFour";

                ProfileEntity profileEntity = new ProfileEntity();
                profileEntity.setId(UUID.randomUUID());
                profileEntity.setFirstname("Test");
                profileEntity.setLastname("Profile");
                profileEntity.setEmail(email);
                profileEntity.setPhoneNumber("0781234567");
                profileEntity.setPassword(password);
                profileEntity.setIsSearcher(true);

                ListingEntity listingEntity = new ListingEntity();
                listingEntity.setId(UUID.randomUUID());
                listingEntity.setLister(profileEntity);
                listingEntity.setTitle("Nice Listing");
                listingEntity.setDescription("Nice Listing with description");
                listingEntity.setStreetName("Kronestutz");
                listingEntity.setStreetNumber("1");
                listingEntity.setZipCode(4500);
                listingEntity.setCityName("Solothurn");
                listingEntity.setPricePerMonth(500);
                listingEntity.setPerfectFlatmateDescription("A person, preferably alive");
                listingEntity.setImagesJson("");
                listingEntity.setCreationDate(LocalDateTime.now());

                List<ListingEntity> listingEntities = new ArrayList<>();
                listingEntities.add(listingEntity);

                ApplicationEntity applicationEntity = new ApplicationEntity();
                applicationEntity.setId(UUID.randomUUID());
                applicationEntity.setListing(listingEntity);
                applicationEntity.setApplicant(profileEntity);
                applicationEntity.setCreationDate(LocalDateTime.now());

                List<ApplicationEntity> applicationEntities = new ArrayList<>();
                applicationEntities.add(applicationEntity);

                Mockito.when(listingService.getListingByProfileId(Mockito.any())).thenReturn(listingEntities);
                Mockito.when(applicationService.getAllApplicationsByListingId(Mockito.any()))
                                .thenReturn(applicationEntities);

                MockHttpServletRequestBuilder getRequest = get(
                                "/profiles/" + profileEntity.getId().toString() + "/listings");

                mockMvc.perform(getRequest).andExpect(status().isOk());
        }
}
