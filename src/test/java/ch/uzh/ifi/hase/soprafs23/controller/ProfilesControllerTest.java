package ch.uzh.ifi.hase.soprafs23.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.uzh.ifi.hase.soprafs23.config.JwtAuthenticationEntryPoint;
import ch.uzh.ifi.hase.soprafs23.config.JwtRequestFilter;
import ch.uzh.ifi.hase.soprafs23.config.JwtTokenUtil;
import ch.uzh.ifi.hase.soprafs23.entity.ApplicationEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ListingEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ProfileEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ProfileLifespanEntity;
import ch.uzh.ifi.hase.soprafs23.repository.ProfileRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.profile.ProfilePutDTO;
import ch.uzh.ifi.hase.soprafs23.service.ApplicationService;
import ch.uzh.ifi.hase.soprafs23.service.BlobUploaderService;
import ch.uzh.ifi.hase.soprafs23.service.ListingService;
import ch.uzh.ifi.hase.soprafs23.service.ProfileLifespanService;
import ch.uzh.ifi.hase.soprafs23.service.ProfileService;

@WebMvcTest(ProfilesController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ContextConfiguration(classes = ProfilesController.class)
class ProfilesControllerTest {

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
                profileEntity.setBirthday(Date.valueOf(LocalDate.now()));
                profileEntity.setPhoneNumber("0781234567");
                profileEntity.setGender("female");
                profileEntity.setBiography("I'm a testy dudette");
                profileEntity.setFutureFlatmatesDescription("I just want someone");
                profileEntity.setPassword(password);
                profileEntity.setIsSearcher(true);
                profileEntity.setEmail(email);

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
                // .andExpect(jsonPath("$.futureFlatmatesDescription")
                // .value(profileEntity.getFutureFlatmatesDescription()))
                // .andExpect(jsonPath("$.lifespans[0].text").value(profileLifespanEntity.getText()));

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
                listingEntity.setAddress("Nictumgasse 7, 4500 Solothurn");
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
        
        static Stream<? extends Arguments> providedArguments() {
                return Stream.of(
                        Arguments.of("test-image.jpg", "profile_document.pdf"),
                        Arguments.of("deleted", "profile_document.pdf"),
                        Arguments.of("test-image.jpg", "deleted")
                );
        }

        @ParameterizedTest
        @MethodSource("providedArguments")
        void updatedProfileByid_parameterized_success(String fileName, String documentName) throws Exception {
                String email = "test.example@gmail.com";
                String password = "OneTwoThreeFour";

                UUID id = UUID.randomUUID();

                ProfileEntity profileEntity = new ProfileEntity();
                profileEntity.setId(id);
                profileEntity.setFirstname("Test");
                profileEntity.setLastname("Profile");
                profileEntity.setBirthday(Date.valueOf(LocalDate.now()));
                profileEntity.setPhoneNumber("0781234567");
                profileEntity.setGender("female");
                profileEntity.setBiography("I'm a testy dudette");
                profileEntity.setFutureFlatmatesDescription("I just want someone");
                profileEntity.setPassword(password);
                profileEntity.setIsSearcher(true);
                profileEntity.setEmail(email);

                ProfilePutDTO profilePutDTO = new ProfilePutDTO();
                profilePutDTO.setFirstname(profileEntity.getFirstname());
                profilePutDTO.setLastname(profileEntity.getLastname());
                profilePutDTO.setBirthday(profileEntity.getBirthday());
                profilePutDTO.setPhoneNumber(profileEntity.getPhoneNumber());
                profilePutDTO.setGender(profileEntity.getGender());
                profilePutDTO.setBiography(profileEntity.getBiography());
                profilePutDTO.setFutureFlatmatesDescription(profileEntity.getFutureFlatmatesDescription());

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

                MockMultipartFile profileFile = new MockMultipartFile(
                                "file",
                                fileName,
                                MediaType.IMAGE_JPEG_VALUE,
                                "test-image.jpg".getBytes());

                MockMultipartFile profileDocument = new MockMultipartFile(
                                "document",
                                documentName,
                                "application/pdf",
                                "binary-document-content".getBytes());

                MockMultipartFile body = new MockMultipartFile(
                                "body",
                                asJsonString(profilePutDTO).getBytes());

                Mockito.when(blobUploaderService.upload(Mockito.any(), Mockito.any(), Mockito.any()))
                                .thenReturn("www.testURL.com");

                MockHttpServletRequestBuilder putRequest =  multipart("/profiles/" + id)
                                .file(profileFile)
                                .file(profileDocument)
                                .file(body);

                                putRequest.with(new RequestPostProcessor() {
                                        @Override
                                        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                                                request.setMethod("PUT");
                                                return request;
                                        }
                                });
                
                mockMvc.perform(putRequest).andExpect(status().isNoContent());
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
