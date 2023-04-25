package ch.uzh.ifi.hase.soprafs23.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ch.uzh.ifi.hase.soprafs23.constant.ListingFilter;
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

    @Test
    void creatListing_validInput_expectSuccess() {
        MockitoAnnotations.openMocks(this);

        ProfileEntity testProfileEntity;

        testProfileEntity = new ProfileEntity();
        testProfileEntity.setId(new UUID(0, 0));
        testProfileEntity.setFirstname("test");
        testProfileEntity.setLastname("name");
        testProfileEntity.setEmail("test.name@example.com");
        testProfileEntity.setPhoneNumber("+41 79 123 45 67");
        testProfileEntity.setPassword("OneTwoThree");
        testProfileEntity.setIsSearcher(false);

        ListingEntity testListingEntity = new ListingEntity();

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

    @Test
    void getListings_filterPrice_expectSuccess() {
        MockitoAnnotations.openMocks(this);

        ProfileEntity testProfileEntity;
        ListingEntity testListingEntityA;
        ListingEntity testListingEntityB;

        testProfileEntity = new ProfileEntity();
        testProfileEntity.setId(new UUID(0, 0));
        testProfileEntity.setFirstname("test");
        testProfileEntity.setLastname("name");
        testProfileEntity.setEmail("test.name@example.com");
        testProfileEntity.setPhoneNumber("+41 79 123 45 67");
        testProfileEntity.setPassword("OneTwoThree");
        testProfileEntity.setIsSearcher(false);

        testListingEntityA = new ListingEntity();
        testListingEntityA.setId(new UUID(0, 0));
        testListingEntityA.setLister(testProfileEntity);
        testListingEntityA.setTitle("cozy apartment");
        testListingEntityA.setDescription("A very nice place to live.");
        testListingEntityA.setStreetName("Ahornweg");
        testListingEntityA.setStreetNumber("27b");
        testListingEntityA.setZipCode(4500);
        testListingEntityA.setCityName("Solothurn");
        testListingEntityA.setPricePerMonth(765.85f);
        // Images.JSON?
        testListingEntityA.setPerfectFlatmateDescription("Some nice person.");

        testListingEntityB = new ListingEntity();
        testListingEntityB.setId(new UUID(0, 0));
        testListingEntityB.setLister(testProfileEntity);
        testListingEntityB.setTitle("spacious apartment");
        testListingEntityB.setDescription("A very nice place to live.");
        testListingEntityB.setStreetName("Ahornweg");
        testListingEntityB.setStreetNumber("27b");
        testListingEntityB.setZipCode(4500);
        testListingEntityB.setCityName("Solothurn");
        testListingEntityB.setPricePerMonth(1765.85f);
        // Images.JSON?
        testListingEntityB.setPerfectFlatmateDescription("Some nice person.");

        List<ListingEntity> testListings = new ArrayList<>();
        testListings.add(testListingEntityA);
        testListings.add(testListingEntityB);
        Mockito.when(listingRepository.findAll()).thenReturn(testListings);

        ListingFilter listingFilter = new ListingFilter();
        listingFilter.setSearchText("apartment");
        listingFilter.setMaxRentPerMonth(1000.0f);
        listingFilter.setFlatmateCapacity(3);

        List<ListingEntity> allListings = listingService.getListings(listingFilter);

        assertEquals(allListings.get(0).getTitle(), testListingEntityA.getTitle());
        assertEquals(allListings.get(1).getTitle(), testListingEntityB.getTitle());
    }

    @Test
    void getListings_filterTextCozy_expectSuccess() {
        MockitoAnnotations.openMocks(this);

        ProfileEntity testProfileEntity;
        ListingEntity testListingEntityA;
        ListingEntity testListingEntityB;

        testProfileEntity = new ProfileEntity();
        testProfileEntity.setId(new UUID(0, 0));
        testProfileEntity.setFirstname("test");
        testProfileEntity.setLastname("name");
        testProfileEntity.setEmail("test.name@example.com");
        testProfileEntity.setPhoneNumber("+41 79 123 45 67");
        testProfileEntity.setPassword("OneTwoThree");
        testProfileEntity.setIsSearcher(false);

        testListingEntityA = new ListingEntity();
        testListingEntityA.setId(new UUID(0, 0));
        testListingEntityA.setLister(testProfileEntity);
        testListingEntityA.setTitle("cozy apartment");
        testListingEntityA.setDescription("A very nice place to live.");
        testListingEntityA.setStreetName("Ahornweg");
        testListingEntityA.setStreetNumber("27b");
        testListingEntityA.setZipCode(4500);
        testListingEntityA.setCityName("Solothurn");
        testListingEntityA.setPricePerMonth(765.85f);
        // Images.JSON?
        testListingEntityA.setPerfectFlatmateDescription("Some nice person.");

        testListingEntityB = new ListingEntity();
        testListingEntityB.setId(new UUID(0, 0));
        testListingEntityB.setLister(testProfileEntity);
        testListingEntityB.setTitle("spacious apartment");
        testListingEntityB.setDescription("A very nice place to live.");
        testListingEntityB.setStreetName("Ahornweg");
        testListingEntityB.setStreetNumber("27b");
        testListingEntityB.setZipCode(4500);
        testListingEntityB.setCityName("Solothurn");
        testListingEntityB.setPricePerMonth(1765.85f);
        // Images.JSON?
        testListingEntityB.setPerfectFlatmateDescription("Some nice person.");

        List<ListingEntity> testListings = new ArrayList<>();
        testListings.add(testListingEntityA);
        testListings.add(testListingEntityB);
        Mockito.when(listingRepository.findAll()).thenReturn(testListings);

        ListingFilter listingFilter = new ListingFilter();
        listingFilter.setSearchText("cozy");
        listingFilter.setMaxRentPerMonth(2000.0f);
        listingFilter.setFlatmateCapacity(3);

        List<ListingEntity> allListings = listingService.getListings(listingFilter);

        assertEquals(allListings.get(0).getTitle(), testListingEntityA.getTitle());
        assertEquals(allListings.get(1).getTitle(), testListingEntityB.getTitle());
    }

    @Test
    void getListings_filterTextSpacious_expectSuccess() {
        MockitoAnnotations.openMocks(this);

        ProfileEntity testProfileEntity;
        ListingEntity testListingEntityA;
        ListingEntity testListingEntityB;

        testProfileEntity = new ProfileEntity();
        testProfileEntity.setId(new UUID(0, 0));
        testProfileEntity.setFirstname("test");
        testProfileEntity.setLastname("name");
        testProfileEntity.setEmail("test.name@example.com");
        testProfileEntity.setPhoneNumber("+41 79 123 45 67");
        testProfileEntity.setPassword("OneTwoThree");
        testProfileEntity.setIsSearcher(false);

        testListingEntityA = new ListingEntity();
        testListingEntityA.setId(new UUID(0, 0));
        testListingEntityA.setLister(testProfileEntity);
        testListingEntityA.setTitle("cozy apartment");
        testListingEntityA.setDescription("A very nice place to live.");
        testListingEntityA.setStreetName("Ahornweg");
        testListingEntityA.setStreetNumber("27b");
        testListingEntityA.setZipCode(4500);
        testListingEntityA.setCityName("Solothurn");
        testListingEntityA.setPricePerMonth(765.85f);
        // Images.JSON?
        testListingEntityA.setPerfectFlatmateDescription("Some nice person.");

        testListingEntityB = new ListingEntity();
        testListingEntityB.setId(new UUID(0, 0));
        testListingEntityB.setLister(testProfileEntity);
        testListingEntityB.setTitle("spacious apartment");
        testListingEntityB.setDescription("A very nice place to live.");
        testListingEntityB.setStreetName("Ahornweg");
        testListingEntityB.setStreetNumber("27b");
        testListingEntityB.setZipCode(4500);
        testListingEntityB.setCityName("Solothurn");
        testListingEntityB.setPricePerMonth(1765.85f);
        // Images.JSON?
        testListingEntityB.setPerfectFlatmateDescription("Some nice person.");

        List<ListingEntity> testListings = new ArrayList<>();
        testListings.add(testListingEntityA);
        testListings.add(testListingEntityB);
        Mockito.when(listingRepository.findAll()).thenReturn(testListings);

        ListingFilter listingFilter = new ListingFilter();
        listingFilter.setSearchText("spacious");
        listingFilter.setMaxRentPerMonth(2000.0f);
        listingFilter.setFlatmateCapacity(3);

        List<ListingEntity> allListings = listingService.getListings(listingFilter);

        assertEquals(allListings.get(1).getTitle(), testListingEntityA.getTitle());
        assertEquals(allListings.get(0).getTitle(), testListingEntityB.getTitle());
    }
}
