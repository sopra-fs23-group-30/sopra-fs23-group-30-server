package ch.uzh.ifi.hase.soprafs23.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs23.constant.ListingFilter;
import ch.uzh.ifi.hase.soprafs23.entity.ListingEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ProfileEntity;
import ch.uzh.ifi.hase.soprafs23.repository.ApplicationRepository;
import ch.uzh.ifi.hase.soprafs23.repository.ListingRepository;

class ListingServiceTest {
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
        testProfileEntity.setId(UUID.randomUUID());
        testProfileEntity.setFirstname("test");
        testProfileEntity.setLastname("name");
        testProfileEntity.setEmail("test.name@example.com");
        testProfileEntity.setPhoneNumber("+41 79 123 45 67");
        testProfileEntity.setPassword("OneTwoThree");
        testProfileEntity.setIsSearcher(false);

        ListingEntity testListingEntity = new ListingEntity();

        testListingEntity.setId(UUID.randomUUID());
        testListingEntity.setLister(testProfileEntity);
        testListingEntity.setTitle("Top tier apartment");
        testListingEntity.setDescription("A very nice place to live.");
        testListingEntity.setStreetName("Ahornweg");
        testListingEntity.setStreetNumber("27b");
        testListingEntity.setZipCode(4500);
        testListingEntity.setCityName("Solothurn");
        testListingEntity.setPricePerMonth(765.85f);
        testListingEntity.setImagesJson("");
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
        testProfileEntity.setId(UUID.randomUUID());
        testProfileEntity.setFirstname("test");
        testProfileEntity.setLastname("name");
        testProfileEntity.setEmail("test.name@example.com");
        testProfileEntity.setPhoneNumber("+41 79 123 45 67");
        testProfileEntity.setPassword("OneTwoThree");
        testProfileEntity.setIsSearcher(false);

        testListingEntityA = new ListingEntity();
        testListingEntityA.setId(UUID.randomUUID());
        testListingEntityA.setLister(testProfileEntity);
        testListingEntityA.setTitle("cozy apartment");
        testListingEntityA.setDescription("A very nice place to live.");
        testListingEntityA.setStreetName("Ahornweg");
        testListingEntityA.setStreetNumber("27b");
        testListingEntityA.setZipCode(4500);
        testListingEntityA.setCityName("Solothurn");
        testListingEntityA.setPricePerMonth(765.85f);
        testListingEntityA.setImagesJson("");
        testListingEntityA.setPerfectFlatmateDescription("Some nice person.");

        testListingEntityB = new ListingEntity();
        testListingEntityB.setId(UUID.randomUUID());
        testListingEntityB.setLister(testProfileEntity);
        testListingEntityB.setTitle("spacious apartment");
        testListingEntityB.setDescription("A very nice place to live.");
        testListingEntityB.setStreetName("Ahornweg");
        testListingEntityB.setStreetNumber("27b");
        testListingEntityB.setZipCode(4500);
        testListingEntityB.setCityName("Solothurn");
        testListingEntityB.setPricePerMonth(1765.85f);
        testListingEntityB.setImagesJson("");
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

        assertTrue(allListings.size() == 1);
        assertEquals(allListings.get(0).getTitle(), testListingEntityA.getTitle());
    }

    @Test
    void getListings_filterTextCozy_expectSuccess() {
        MockitoAnnotations.openMocks(this);

        ProfileEntity testProfileEntity;
        ListingEntity testListingEntityA;
        ListingEntity testListingEntityB;

        testProfileEntity = new ProfileEntity();
        testProfileEntity.setId(UUID.randomUUID());
        testProfileEntity.setFirstname("test");
        testProfileEntity.setLastname("name");
        testProfileEntity.setEmail("test.name@example.com");
        testProfileEntity.setPhoneNumber("+41 79 123 45 67");
        testProfileEntity.setPassword("OneTwoThree");
        testProfileEntity.setIsSearcher(false);

        testListingEntityA = new ListingEntity();
        testListingEntityA.setId(UUID.randomUUID());
        testListingEntityA.setLister(testProfileEntity);
        testListingEntityA.setTitle("cozy apartment");
        testListingEntityA.setDescription("A very nice place to live.");
        testListingEntityA.setStreetName("Ahornweg");
        testListingEntityA.setStreetNumber("27b");
        testListingEntityA.setZipCode(4500);
        testListingEntityA.setCityName("Solothurn");
        testListingEntityA.setPricePerMonth(765.85f);
        testListingEntityA.setImagesJson("");
        testListingEntityA.setPerfectFlatmateDescription("Some nice person.");

        testListingEntityB = new ListingEntity();
        testListingEntityB.setId(UUID.randomUUID());
        testListingEntityB.setLister(testProfileEntity);
        testListingEntityB.setTitle("spacious apartment");
        testListingEntityB.setDescription("A very nice place to live.");
        testListingEntityB.setStreetName("Ahornweg");
        testListingEntityB.setStreetNumber("27b");
        testListingEntityB.setZipCode(4500);
        testListingEntityB.setCityName("Solothurn");
        testListingEntityB.setPricePerMonth(1765.85f);
        testListingEntityB.setImagesJson("");
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
        testProfileEntity.setId(UUID.randomUUID());
        testProfileEntity.setFirstname("test");
        testProfileEntity.setLastname("name");
        testProfileEntity.setEmail("test.name@example.com");
        testProfileEntity.setPhoneNumber("+41 79 123 45 67");
        testProfileEntity.setPassword("OneTwoThree");
        testProfileEntity.setIsSearcher(false);

        testListingEntityA = new ListingEntity();
        testListingEntityA.setId(UUID.randomUUID());
        testListingEntityA.setLister(testProfileEntity);
        testListingEntityA.setTitle("cozy apartment");
        testListingEntityA.setDescription("A very nice place to live.");
        testListingEntityA.setStreetName("Ahornweg");
        testListingEntityA.setStreetNumber("27b");
        testListingEntityA.setZipCode(4500);
        testListingEntityA.setCityName("Solothurn");
        testListingEntityA.setPricePerMonth(765.85f);
        testListingEntityA.setImagesJson("");
        testListingEntityA.setPerfectFlatmateDescription("Some nice person.");

        testListingEntityB = new ListingEntity();
        testListingEntityB.setId(UUID.randomUUID());
        testListingEntityB.setLister(testProfileEntity);
        testListingEntityB.setTitle("spacious apartment");
        testListingEntityB.setDescription("A very nice place to live.");
        testListingEntityB.setStreetName("Ahornweg");
        testListingEntityB.setStreetNumber("27b");
        testListingEntityB.setZipCode(4500);
        testListingEntityB.setCityName("Solothurn");
        testListingEntityB.setPricePerMonth(1765.85f);
        testListingEntityB.setImagesJson("");
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

    @Test
    void getListingsById_validInput_expectSuccess() {
        MockitoAnnotations.openMocks(this);

        ProfileEntity testProfileEntity;

        testProfileEntity = new ProfileEntity();
        testProfileEntity.setId(UUID.randomUUID());
        testProfileEntity.setFirstname("test");
        testProfileEntity.setLastname("name");
        testProfileEntity.setEmail("test.name@example.com");
        testProfileEntity.setPhoneNumber("+41 79 123 45 67");
        testProfileEntity.setPassword("OneTwoThree");
        testProfileEntity.setIsSearcher(false);

        ListingEntity testListingEntity = new ListingEntity();

        testListingEntity.setId(UUID.randomUUID());
        testListingEntity.setLister(testProfileEntity);
        testListingEntity.setTitle("Top tier apartment");
        testListingEntity.setDescription("A very nice place to live.");
        testListingEntity.setStreetName("Ahornweg");
        testListingEntity.setStreetNumber("27b");
        testListingEntity.setZipCode(4500);
        testListingEntity.setCityName("Solothurn");
        testListingEntity.setPricePerMonth(765.85f);
        testListingEntity.setImagesJson("");
        testListingEntity.setPerfectFlatmateDescription("Some nice person.");

        Mockito.when(listingRepository.findById(testListingEntity.getId()))
                .thenReturn(Optional.of(testListingEntity));
        ListingEntity foundListingEntity = listingService.getListingById(testListingEntity.getId());

        assertEquals(testListingEntity.getId(), foundListingEntity.getId());
        assertEquals(testListingEntity.getLister(), foundListingEntity.getLister());
        assertEquals(testListingEntity.getTitle(), foundListingEntity.getTitle());
        assertEquals(testListingEntity.getDescription(), foundListingEntity.getDescription());
        assertEquals(testListingEntity.getStreetName(), foundListingEntity.getStreetName());
        assertEquals(testListingEntity.getStreetNumber(), foundListingEntity.getStreetNumber());
        assertEquals(testListingEntity.getZipCode(), foundListingEntity.getZipCode());
        assertEquals(testListingEntity.getCityName(), foundListingEntity.getCityName());
        assertEquals(testListingEntity.getPricePerMonth(), foundListingEntity.getPricePerMonth());
        assertEquals(testListingEntity.getPerfectFlatmateDescription(),
                foundListingEntity.getPerfectFlatmateDescription());
    }

    @Test
    void getListingsById_unknownInput_expectEmpty() {
        MockitoAnnotations.openMocks(this);

        ProfileEntity testProfileEntity;

        testProfileEntity = new ProfileEntity();
        testProfileEntity.setId(UUID.randomUUID());
        testProfileEntity.setFirstname("test");
        testProfileEntity.setLastname("name");
        testProfileEntity.setEmail("test.name@example.com");
        testProfileEntity.setPhoneNumber("+41 79 123 45 67");
        testProfileEntity.setPassword("OneTwoThree");
        testProfileEntity.setIsSearcher(false);

        ListingEntity testListingEntity = new ListingEntity();

        testListingEntity.setId(UUID.randomUUID());
        testListingEntity.setLister(testProfileEntity);
        testListingEntity.setTitle("Top tier apartment");
        testListingEntity.setDescription("A very nice place to live.");
        testListingEntity.setStreetName("Ahornweg");
        testListingEntity.setStreetNumber("27b");
        testListingEntity.setZipCode(4500);
        testListingEntity.setCityName("Solothurn");
        testListingEntity.setPricePerMonth(765.85f);
        testListingEntity.setImagesJson("");
        testListingEntity.setPerfectFlatmateDescription("Some nice person.");

        Mockito.when(listingRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        UUID randomUUID = UUID.randomUUID();
        assertThrows(ResponseStatusException.class, () -> listingService.getListingById(randomUUID));
    }
}
