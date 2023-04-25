package ch.uzh.ifi.hase.soprafs23.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import ch.uzh.ifi.hase.soprafs23.entity.ProfileEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ProfileLifespanEntity;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DataJpaTest
public class ProfileLifespanRepositoryTest {
    private ProfileEntity profileEntity;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProfileLifespanRepository profileLifespanRepository;

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
    void findByProfileId_validInput_success() {
        ProfileLifespanEntity profileLifespanEntity = new ProfileLifespanEntity();
        profileLifespanEntity.setProfile(profileEntity);
        profileLifespanEntity.setIsExperience(true);
        profileLifespanEntity.setText("Truly amazing experiences");
        profileLifespanEntity.setFromDate(Date.valueOf("1848-02-17"));
        profileLifespanEntity.setToDate(Date.valueOf("2002-12-26"));
        entityManager.persist(profileLifespanEntity);
        entityManager.flush();

        List<ProfileLifespanEntity> searched = profileLifespanRepository.findByProfileId(profileEntity.getId());
        assertTrue(searched.size() >= 1);

        ProfileLifespanEntity found = searched.get(0);
        assertEquals(profileLifespanEntity.getId(), found.getId());
        assertEquals(profileLifespanEntity.getProfile(), found.getProfile());
        assertEquals(profileLifespanEntity.getIsExperience(), found.getIsExperience());
        assertEquals(profileLifespanEntity.getText(), found.getText());
        assertEquals(profileLifespanEntity.getFromDate(), found.getFromDate());
        assertEquals(profileLifespanEntity.getToDate(), found.getToDate());
    }

    @Test
    void findByProfileId_unknownInput_success() {
        ProfileLifespanEntity profileLifespanEntity = new ProfileLifespanEntity();
        profileLifespanEntity.setProfile(profileEntity);
        profileLifespanEntity.setIsExperience(true);
        profileLifespanEntity.setText("Truly amazing experiences");
        profileLifespanEntity.setFromDate(Date.valueOf("1848-02-17"));
        profileLifespanEntity.setToDate(Date.valueOf("2002-12-26"));
        entityManager.persist(profileLifespanEntity);
        entityManager.flush();

        List<ProfileLifespanEntity> searched = profileLifespanRepository.findByProfileId(new UUID(1, 1));
        assertTrue(searched.size() == 0);
    }
}
