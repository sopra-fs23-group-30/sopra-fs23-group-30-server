package ch.uzh.ifi.hase.soprafs23.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import ch.uzh.ifi.hase.soprafs23.entity.ListingEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ProfileEntity;
import ch.uzh.ifi.hase.soprafs23.rest.dto.listing.ListingPostDTO;
import ch.uzh.ifi.hase.soprafs23.service.BlobUploaderService;
import ch.uzh.ifi.hase.soprafs23.service.ListingService;
import ch.uzh.ifi.hase.soprafs23.service.ProfileService;

@WebMvcTest(ListingsController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ContextConfiguration(classes = ListingsController.class)
class ListingsControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private ProfileService profileService;

        @MockBean
        private ListingService listingService;

        @MockBean
        private BlobUploaderService blobUploaderService;

        private ListingEntity testListing;
        private ProfileEntity testProfileEntity;

        @BeforeEach
        void setup() {
                mockMvc = MockMvcBuilders
                                .standaloneSetup(new ListingsController(listingService, profileService,
                                                blobUploaderService))
                                .build();

                testProfileEntity = new ProfileEntity();
                testProfileEntity.setId(UUID.randomUUID());

                testListing = new ListingEntity();
                testListing.setId(UUID.randomUUID());
                testListing.setTitle("Nice Listing");
                testListing.setDescription("Nice Listing with description");
                testListing.setAddress("Kronengasse 1, 4500 Solothurn");
                testListing.setLattitude(47);
                testListing.setLongitude(7);
                testListing.setPricePerMonth(500);
                testListing.setPerfectFlatmateDescription("A person, preferably alive");
                testListing.setLister(testProfileEntity);
                testListing.setImagesJson("");
                testListing.setCreationDate(LocalDateTime.now());
                testListing.setPetsAllowed(true);
                testListing.setElevator(true);
                testListing.setDishwasher(true);
        }

        @Test
        void createdListing_validInput_success() throws Exception {
                ListingPostDTO listingPostDTO = new ListingPostDTO();
                listingPostDTO.setTitle(testListing.getTitle());
                listingPostDTO.setDescription(testListing.getDescription());
                listingPostDTO.setAddress(testListing.getAddress());
                listingPostDTO.setLattitude(testListing.getLattitude());
                listingPostDTO.setLongitude(testListing.getLongitude());
                listingPostDTO.setPricePerMonth(testListing.getPricePerMonth());
                listingPostDTO.setPerfectFlatmateDescription(testListing.getPerfectFlatmateDescription());
                listingPostDTO.setListerId(testListing.getLister().getId());
                listingPostDTO.setImagesJson(testListing.getImagesJson());
                listingPostDTO.setPetsAllowed(testListing.getPetsAllowed());
                listingPostDTO.setElevator(testListing.getElevator());
                listingPostDTO.setDishwasher(testListing.getDishwasher());

                Mockito.when(profileService.getProfileById(listingPostDTO.getListerId()))
                                .thenReturn(testProfileEntity);
                Mockito.when(listingService.createListing(Mockito.any()))
                                .thenReturn(testListing);
                MockMultipartFile file = new MockMultipartFile(
                                "files",
                                "test-image.jpg",
                                MediaType.IMAGE_JPEG_VALUE,
                                "test-image.jpg".getBytes());
                MockMultipartFile body = new MockMultipartFile(
                                "body",
                                asJsonString(listingPostDTO).getBytes());
                MockHttpServletRequestBuilder postRequest = multipart("/listings")
                                .file(file)
                                .file(body);

                mockMvc.perform(postRequest).andExpect(status().isCreated());
        }

        @Test
        void createdListing_validInput_badRequest() throws Exception {
                ListingPostDTO listingPostDTO = new ListingPostDTO();
                listingPostDTO.setTitle(testListing.getTitle());
                listingPostDTO.setDescription(testListing.getDescription());
                listingPostDTO.setAddress(testListing.getAddress());
                listingPostDTO.setLattitude(testListing.getLattitude());
                listingPostDTO.setLongitude(testListing.getLongitude());
                listingPostDTO.setPricePerMonth(testListing.getPricePerMonth());
                listingPostDTO.setPerfectFlatmateDescription(testListing.getPerfectFlatmateDescription());
                listingPostDTO.setListerId(testListing.getLister().getId());
                listingPostDTO.setImagesJson(testListing.getImagesJson());
                listingPostDTO.setPetsAllowed(testListing.getPetsAllowed());
                listingPostDTO.setElevator(testListing.getElevator());
                listingPostDTO.setDishwasher(testListing.getDishwasher());
                Mockito.when(profileService.getProfileById(listingPostDTO.getListerId()))
                                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "For the provided profile id no profile was found"));
                Mockito.when(listingService.createListing(Mockito.any()))
                                .thenReturn(testListing);
                MockMultipartFile file = new MockMultipartFile(
                                "files",
                                "test-image.jpg",
                                MediaType.IMAGE_JPEG_VALUE,
                                "test-image.jpg".getBytes());
                MockMultipartFile body = new MockMultipartFile(
                                "body",
                                asJsonString(listingPostDTO).getBytes());

                MockHttpServletRequestBuilder postRequest = multipart("/listings")
                                .file(file)
                                .file(body);
                mockMvc.perform(postRequest).andExpect(status().isNotFound());
        }

        @Test
        // @WithMockUser
        void getListings_validInput_thenSuccess() throws Exception {
                String searchText = "apartment";
                Float maxRentPerMonth = 2000.0f;
                String sortBy = "PRICE_ASCENDING";
                Boolean petsAllowed = true;
                Boolean elevator = true;
                Boolean dishwasher = true;

                MockHttpServletRequestBuilder getRequest = get("/listings")
                                .param("searchText", searchText)
                                .param("maxRentPerMonth", maxRentPerMonth.toString())
                                .param("sortBy", sortBy)
                                .param("petsAllowed", petsAllowed.toString())
                                .param("elevator", elevator.toString())
                                .param("dishwasher", dishwasher.toString());

                mockMvc.perform(getRequest)
                                .andExpect(status().isOk());
        }

        @Test
        // @WithMockUser
        void getListingById_validInput_thenSuccess() throws Exception {
                Mockito.when(listingService.getListingById(Mockito.any())).thenReturn(testListing);

                MockHttpServletRequestBuilder getRequest = get("/listings/" + testListing.getId().toString());

                mockMvc.perform(getRequest)
                                .andExpect(status().isOk());
        }

        @Test
        // @WithMockUser
        void deleteListingById_validInput_thenSuccess() throws Exception {
                MockHttpServletRequestBuilder deleteRequest = delete("/listings/" + testListing.getId().toString());

                mockMvc.perform(deleteRequest)
                                .andExpect(status().isNoContent());
        }

        @Test
        // @WithMockUser
        void getListingPreviews_validInput_thenSuccess() throws Exception {
                MockHttpServletRequestBuilder getRequest = get("/listingpreviews");

                mockMvc.perform(getRequest)
                                .andExpect(status().isOk());
        }

        @Test
        void updateProfileByid_validInput_thenSuccess() throws Exception {
                ListingPostDTO listingPutDTO = new ListingPostDTO();
                listingPutDTO.setTitle(testListing.getTitle());
                listingPutDTO.setDescription(testListing.getDescription());
                listingPutDTO.setAddress(testListing.getAddress());
                listingPutDTO.setLattitude(testListing.getLattitude());
                listingPutDTO.setLongitude(testListing.getLongitude());
                listingPutDTO.setPricePerMonth(testListing.getPricePerMonth());
                listingPutDTO.setPerfectFlatmateDescription(testListing.getPerfectFlatmateDescription());
                listingPutDTO.setListerId(testListing.getLister().getId());
                listingPutDTO.setImagesJson(testListing.getImagesJson());
                listingPutDTO.setPetsAllowed(testListing.getPetsAllowed());
                listingPutDTO.setElevator(testListing.getElevator());
                listingPutDTO.setDishwasher(testListing.getDishwasher());

                MockMultipartFile file1 = new MockMultipartFile(
                                "files",
                                "test-image-1.jpg",
                                MediaType.IMAGE_JPEG_VALUE,
                                "test-image.jpg".getBytes());

                MockMultipartFile file2 = new MockMultipartFile(
                                "files",
                                "test-image-2.jpg",
                                MediaType.IMAGE_JPEG_VALUE,
                                "test-image.jpg".getBytes());

                MockMultipartFile body = new MockMultipartFile(
                                "body",
                                asJsonString(listingPutDTO).getBytes());

                Mockito.when(listingService.getListingById(Mockito.any())).thenReturn(testListing);

                List<String> blobURLs = new ArrayList<>();
                blobURLs.add("www.testURL.com");

                Mockito.when(blobUploaderService.uploadImages(Mockito.any(), Mockito.any(), Mockito.any()))
                                .thenReturn(blobURLs);

                MockHttpServletRequestBuilder putRequest = multipart("/listings/" + testListing.getId())
                                .file(file1)
                                .file(file2)
                                .file(body);

                putRequest.with(new RequestPostProcessor() {
                        @Override
                        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                                request.setMethod("PUT");
                                return request;
                        }
                });

                mockMvc.perform(putRequest)
                                .andExpect(status().isNoContent());
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
