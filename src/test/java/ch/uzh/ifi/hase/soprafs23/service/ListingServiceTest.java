package ch.uzh.ifi.hase.soprafs23.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ch.uzh.ifi.hase.soprafs23.entity.ListingEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ProfileEntity;
import ch.uzh.ifi.hase.soprafs23.repository.ApplicationRepository;
import ch.uzh.ifi.hase.soprafs23.repository.ListingRepository;

public class ListingServiceTest {
    @Mock
    private ListingRepository listingRepository;
    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private ListingService listingService;

    private ListingEntity testListingEntity;
    private ProfileEntity testProfileEntity;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        testProfileEntity = new ProfileEntity();
        testProfileEntity.setId(new UUID(0, 0));
        testProfileEntity.setFirstname("test");
        testProfileEntity.setLastname("name");
        testProfileEntity.setEmail("test.name@example.com");
        testProfileEntity.setPhoneNumber("+41 79 123 45 67");
        testProfileEntity.setPassword("OneTwoThree");
        testProfileEntity.setIsSearcher(false);

        testListingEntity = new ListingEntity();

        testListingEntity.setId(new UUID(0, 0));
        testListingEntity.setLister(testProfileEntity);
        testListingEntity.setTitle("Top tier apartment");
        testListingEntity.setDescription("A very nice place to live.");
        testListingEntity.setStreetName("Ahornweg");
        testListingEntity.setStreetNumber("27b");
        testListingEntity.setZipCode(4500);
        testListingEntity.setCityName("Solothurn");
        testListingEntity.setPricePerMonth(765.85f);
        // Images.JSON?
        testListingEntity.setPerfectFlatmateDescription("Some nice person.");

        Mockito.when(listingRepository.save(Mockito.any())).thenReturn(testListingEntity);
    }

    @Test
    void creatListing_validInput_expectSucces() {
        ListingEntity createdListingEntity = listingService.createListing(testListingEntity);

        assertEquals(testListingEntity.getId(), createdListingEntity.getId());
        assertEquals(testListingEntity.getLister(), createdListingEntity.getLister());
        assertEquals(testListingEntity.getTitle(), createdListingEntity.getTitle());
        assertEquals(testListingEntity.getDescription(), createdListingEntity.getDescription());
        assertEquals(testListingEntity.getStreetName(), createdListingEntity.getStreetName());
        assertEquals(testListingEntity.getStreetNumber(), createdListingEntity.getStreetNumber());
        assertEquals(testListingEntity.getZipCode(), createdListingEntity.getZipCode());
        assertEquals(testListingEntity.getCityName(), createdListingEntity.getCityName());
        assertEquals(testListingEntity.getPricePerMonth(), createdListingEntity.getPricePerMonth());
        assertEquals(testListingEntity.getPerfectFlatmateDescription(),
                createdListingEntity.getPerfectFlatmateDescription());
    }
}
