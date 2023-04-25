package ch.uzh.ifi.hase.soprafs23.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import ch.uzh.ifi.hase.soprafs23.entity.ProfileEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DataJpaTest
public class ProfileRepositoryIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ProfileRepository profileRepository;

    @Test
    public void findByEMail_success() {
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setFirstname("Firstname");
        profileEntity.setLastname("Lastname");
        profileEntity.setEmail("firstname.lastname@example.ch");
        profileEntity.setPhoneNumber("0781234567");
        profileEntity.setIsSearcher(false);
        profileEntity.setPassword("OneTwoThreeFour");
        entityManager.persist(profileEntity);
        entityManager.flush();

        ProfileEntity found = profileRepository.findByEmail(profileEntity.getEmail());

        assertNotNull(found.getId());
        assertEquals(found.getFirstname(), profileEntity.getFirstname());
        assertEquals(found.getLastname(), profileEntity.getLastname());
        assertEquals(found.getEmail(), profileEntity.getEmail());
        assertEquals(found.getPhoneNumber(), profileEntity.getPhoneNumber());
        assertEquals(found.getIsSearcher(), profileEntity.getIsSearcher());
        assertEquals(found.getPassword(), profileEntity.getPassword());
    }

    @Test
    public void findByID_success() {
        ProfileEntity profile = new ProfileEntity();
        profile.setFirstname("Firstname");
        profile.setLastname("Lastname");
        profile.setEmail("firstname.lastname@example.ch");
        profile.setPhoneNumber("0781234567");
        profile.setIsSearcher(false);
        profile.setPassword("OneTwoThreeFour");
        entityManager.persist(profile);
        entityManager.flush();

        Optional<ProfileEntity> searched = profileRepository.findById(profile.getId());

        assertTrue(searched.isPresent());

        ProfileEntity found = searched.get();

        assertNotNull(found.getId());
        assertEquals(found.getFirstname(), profile.getFirstname());
        assertEquals(found.getLastname(), profile.getLastname());
        assertEquals(found.getEmail(), profile.getEmail());
        assertEquals(found.getPhoneNumber(), profile.getPhoneNumber());
        assertEquals(found.getIsSearcher(), profile.getIsSearcher());
        assertEquals(found.getPassword(), profile.getPassword());
    }
}