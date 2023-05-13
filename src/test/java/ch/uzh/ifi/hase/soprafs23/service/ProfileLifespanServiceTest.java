package ch.uzh.ifi.hase.soprafs23.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ch.uzh.ifi.hase.soprafs23.entity.ProfileEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ProfileLifespanEntity;
import ch.uzh.ifi.hase.soprafs23.repository.ProfileLifespanRepository;
import ch.uzh.ifi.hase.soprafs23.repository.ProfileRepository;

class ProfileLifespanServiceTest {

    @Mock
    private ProfileLifespanRepository profileLifespanRepository;

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileLifespanService profileLifespanService;

    private ProfileLifespanEntity testProfileLifespanEntity;
    private ProfileEntity testProfile;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        testProfile = new ProfileEntity();
        testProfile.setId(UUID.randomUUID());
        testProfile.setFirstname("test");
        testProfile.setLastname("name");
        testProfile.setEmail("test.name@example.com");
        testProfile.setPhoneNumber("+41 79 123 45 67");
        testProfile.setPassword("OneTwoThree");
        testProfile.setIsSearcher(false);

        testProfileLifespanEntity = new ProfileLifespanEntity();
        testProfileLifespanEntity.setProfile(testProfile);
        testProfileLifespanEntity.setIsExperience(true);
        testProfileLifespanEntity.setText("Truly amazing experiences");
        testProfileLifespanEntity.setFromDate(Date.valueOf("1848-02-17"));
        testProfileLifespanEntity.setToDate(Date.valueOf("2002-12-26"));
    }

    @Test
    void getAllLifespansByProfileId_existingId_success() {

        testProfile = new ProfileEntity();
        testProfile.setId(UUID.randomUUID());
        testProfile.setFirstname("test");
        testProfile.setLastname("name");
        testProfile.setEmail("test.name@example.com");
        testProfile.setPhoneNumber("+41 79 123 45 67");
        testProfile.setPassword("OneTwoThree");
        testProfile.setIsSearcher(false);

        testProfileLifespanEntity = new ProfileLifespanEntity();
        testProfileLifespanEntity.setProfile(testProfile);
        testProfileLifespanEntity.setIsExperience(true);
        testProfileLifespanEntity.setText("Truly amazing experiences");
        testProfileLifespanEntity.setFromDate(Date.valueOf("1848-02-17"));
        testProfileLifespanEntity.setToDate(Date.valueOf("2002-12-26"));

        List<ProfileLifespanEntity> testProfileLifespanEntities = new ArrayList<>();
        testProfileLifespanEntities.add(testProfileLifespanEntity);

        Mockito.when(profileLifespanRepository.findByProfileId(testProfile.getId()))
                .thenReturn(testProfileLifespanEntities);

        ProfileLifespanEntity found = profileLifespanService.getAllLifespansByProfileId(testProfile.getId()).get(0);

        assertEquals(testProfileLifespanEntity.getProfile(), found.getProfile());
        assertEquals(testProfileLifespanEntity.getIsExperience(), found.getIsExperience());
        assertEquals(testProfileLifespanEntity.getText(), found.getText());
        assertEquals(testProfileLifespanEntity.getFromDate(), found.getFromDate());
        assertEquals(testProfileLifespanEntity.getToDate(), found.getToDate());
    }
}