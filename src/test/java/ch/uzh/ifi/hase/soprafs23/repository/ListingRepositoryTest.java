package ch.uzh.ifi.hase.soprafs23.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import ch.uzh.ifi.hase.soprafs23.entity.ListingEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ProfileEntity;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DataJpaTest
class ListingRepositoryTest {
    private ProfileEntity profileEntity;

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ListingRepository listingRepository;

    @BeforeEach
    void setup() {
        profileEntity = new ProfileEntity();
        profileEntity.setFirstname("test");
        profileEntity.setLastname("name");
        profileEntity.setEmail("test.name@example.com");
        profileEntity.setPhoneNumber("+41 79 123 45 67");
        profileEntity.setPassword("OneTwoThree");
        profileEntity.setIsSearcher(false);
        entityManager.persist(profileEntity);
        entityManager.flush();
    }

    @Test
    void findById_validInput_success() {
        ListingEntity listingEntity = new ListingEntity();
        listingEntity.setLister(profileEntity);
        listingEntity.setTitle("Top tier apartment");
        listingEntity.setDescription("A very nice place to live.");
        listingEntity.setAddress("Friedhofplatz 8, 4500 Solothurn");
        listingEntity.setPricePerMonth(765.85f);
        listingEntity.setImagesJson("");
        listingEntity.setPerfectFlatmateDescription("Some person");
        entityManager.persist(listingEntity);
        entityManager.flush();

        Optional<ListingEntity> searched = listingRepository.findById(listingEntity.getId());
        assertTrue(searched.isPresent());

        ListingEntity found = searched.get();
        assertEquals(listingEntity.getId(), found.getId());
        assertEquals(listingEntity.getLister(), found.getLister());
        assertEquals(listingEntity.getTitle(), found.getTitle());
        assertEquals(listingEntity.getDescription(), found.getDescription());
        assertEquals(listingEntity.getAddress(), found.getAddress());
        assertEquals(listingEntity.getPricePerMonth(), found.getPricePerMonth());
        assertEquals(listingEntity.getImagesJson(), found.getImagesJson());
        assertEquals(listingEntity.getPerfectFlatmateDescription(), found.getPerfectFlatmateDescription());
    }

    @Test
    void findByListerId_unknownInput_emptyOptional() {
        Optional<ListingEntity> searched = listingRepository.findById(new UUID(1, 1));
        assertTrue(searched.isEmpty());
    }

    @Test
    void findByListerId_validInput_success() {
        ListingEntity listingEntity = new ListingEntity();
        listingEntity.setLister(profileEntity);
        listingEntity.setTitle("Top tier apartment");
        listingEntity.setDescription("A very nice place to live.");
        listingEntity.setAddress("Friedhofplatz 8, 4500 Solothurn");
        listingEntity.setPricePerMonth(765.85f);
        listingEntity.setImagesJson("");
        listingEntity.setPerfectFlatmateDescription("Some person");
        entityManager.persist(listingEntity);
        entityManager.flush();

        List<ListingEntity> searched = listingRepository.findByListerId(listingEntity.getLister().getId());
        assertTrue(searched.size() >= 1);

        ListingEntity found = searched.get(0);
        assertEquals(listingEntity.getId(), found.getId());
        assertEquals(listingEntity.getLister(), found.getLister());
        assertEquals(listingEntity.getTitle(), found.getTitle());
        assertEquals(listingEntity.getDescription(), found.getDescription());
        assertEquals(listingEntity.getAddress(), found.getAddress());
        assertEquals(listingEntity.getPricePerMonth(), found.getPricePerMonth());
        assertEquals(listingEntity.getImagesJson(), found.getImagesJson());
        assertEquals(listingEntity.getPerfectFlatmateDescription(), found.getPerfectFlatmateDescription());
    }

    @Test
    void findById_unknownInput_emptyOptional() {
        List<ListingEntity> searched = listingRepository.findByListerId(new UUID(1, 1));
        assertEquals(0, searched.size());
    }
}
