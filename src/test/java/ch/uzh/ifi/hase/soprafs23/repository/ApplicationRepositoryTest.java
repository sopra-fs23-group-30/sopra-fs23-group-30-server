package ch.uzh.ifi.hase.soprafs23.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ch.uzh.ifi.hase.soprafs23.entity.ApplicationEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ListingEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ProfileEntity;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DataJpaTest
class ApplicationRepositoryTest {

    private ListingEntity listingEntity;
    private ProfileEntity profileEntity;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ApplicationRepository applicationRepository;

    @BeforeEach
    void setup() {
        ProfileEntity lister = new ProfileEntity();
        lister.setFirstname("testlister");
        lister.setLastname("name");
        lister.setEmail("lister.name@example.com");
        lister.setPhoneNumber("+41 79 123 45 67");
        lister.setPassword("OneTwoThree");
        lister.setIsSearcher(false);
        entityManager.persist(lister);
        entityManager.flush();

        listingEntity = new ListingEntity();
        listingEntity.setLister(lister);
        listingEntity.setTitle("Top tier apartment");
        listingEntity.setDescription("A very nice place to live.");
        listingEntity.setAddress("Ahronweg 77, 4543 Deitingen");
        listingEntity.setPricePerMonth(765.85f);
        listingEntity.setImagesJson("");
        listingEntity.setPerfectFlatmateDescription("Some person");
        entityManager.persist(listingEntity);
        entityManager.flush();
        
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
    void findByID_success() {
        ApplicationEntity applicationEntity = new ApplicationEntity();

        applicationEntity.setListing(listingEntity);
        applicationEntity.setApplicant(profileEntity);
        entityManager.persist(applicationEntity);
        entityManager.flush();

        Optional<ApplicationEntity> searched = applicationRepository.findById(applicationEntity.getId());
        assertTrue(searched.isPresent());

        ApplicationEntity found = searched.get();
        assertNotNull(found.getId());
        assertEquals(found.getListing(), applicationEntity.getListing());
        assertEquals(found.getApplicant(), applicationEntity.getApplicant());
        assertEquals(found.getCreationDate(), applicationEntity.getCreationDate());
        assertEquals(found.getState(), applicationEntity.getState());
    }

    @Test
    void findByApplicantId_success() {
        ApplicationEntity applicationEntity = new ApplicationEntity();

        applicationEntity.setListing(listingEntity);
        applicationEntity.setApplicant(profileEntity);
        entityManager.persist(applicationEntity);
        entityManager.flush();

        List<ApplicationEntity> searched = applicationRepository.findByApplicantId(profileEntity.getId());
        assertTrue(searched.size() >= 1);

        ApplicationEntity found = searched.get(0);
        assertNotNull(found.getId());
        assertEquals(found.getListing(), applicationEntity.getListing());
        assertEquals(found.getApplicant(), applicationEntity.getApplicant());
        assertEquals(found.getCreationDate(), applicationEntity.getCreationDate());
        assertEquals(found.getState(), applicationEntity.getState());
    }

    @Test
    void findByListingId_success() {
        ApplicationEntity applicationEntity = new ApplicationEntity();

        applicationEntity.setListing(listingEntity);
        applicationEntity.setApplicant(profileEntity);
        entityManager.persist(applicationEntity);
        entityManager.flush();

        List<ApplicationEntity> searched = applicationRepository.findByListingId(listingEntity.getId());
        assertTrue(searched.size() >= 1);

        ApplicationEntity found = searched.get(0);
        assertNotNull(found.getId());
        assertEquals(found.getListing(), applicationEntity.getListing());
        assertEquals(found.getApplicant(), applicationEntity.getApplicant());
        assertEquals(found.getCreationDate(), applicationEntity.getCreationDate());
        assertEquals(found.getState(), applicationEntity.getState());
    }
}
