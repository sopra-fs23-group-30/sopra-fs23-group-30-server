package ch.uzh.ifi.hase.soprafs23.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ch.uzh.ifi.hase.soprafs23.entity.ListingEntity;
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

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ListingsController(listingService, profileService, blobUploaderService))
                .build();

        testListing = new ListingEntity();
        testListing.setId(new UUID(0, 0));
        testListing.setCreationDate(LocalDateTime.now());
    }

    @Test
    // @WithMockUser
    void getListings_validInput_thenSuccess() throws Exception {
        String searchText = "apartment";
        Float maxRentPerMonth = 2000.0f;
        Integer flatmateCapacity = 10;

        MockHttpServletRequestBuilder getRequest = get("/listings")
                .contentType(MediaType.APPLICATION_JSON)
                .param("searchText", searchText)
                .param("maxRentPerMonth", maxRentPerMonth.toString())
                .param("flatmateCapacity", flatmateCapacity.toString());
        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }
}
