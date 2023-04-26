package ch.uzh.ifi.hase.soprafs23.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs23.entity.ProfileEntity;
import ch.uzh.ifi.hase.soprafs23.repository.ProfileRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.profile.ProfileLifespanDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.profile.ProfilePutDTO;

/**
 * Test class for the UserResource REST resource.
 *
 * @see ProfileService
 */
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@WebAppConfiguration
@SpringBootTest
public class ProfileServiceIntegrationTest {
    @Qualifier("profileRepository")
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProfileService profileService;

    @BeforeEach
    public void setup() {
        profileRepository.deleteAll();
    }

    @Test
    void createUser_validInputs_success() {
        // given
        assertNull(profileRepository.findByEmail("User.Name@example.ch"));
        ProfileEntity testProfile = new ProfileEntity();
        testProfile.setFirstname("User");
        testProfile.setLastname("Name");
        testProfile.setEmail("User.Name@example.ch");
        testProfile.setPhoneNumber("+41 78 123 45 67");
        testProfile.setPassword("OneTwoThreeFour");
        testProfile.setIsSearcher(false);
        // when
        ProfileEntity createdProfile = profileService.createUser(testProfile);
        // then
        assertEquals(testProfile.getId(), createdProfile.getId());
        assertEquals(testProfile.getFirstname(), createdProfile.getFirstname());
        assertEquals(testProfile.getLastname(), createdProfile.getLastname());
        assertEquals(testProfile.getEmail(), createdProfile.getEmail());
        assertEquals(testProfile.getPhoneNumber(), createdProfile.getPhoneNumber());
        assertEquals(testProfile.getPassword(), createdProfile.getPassword());
        assertEquals(testProfile.getIsSearcher(), createdProfile.getIsSearcher());
    }

    @Test
    void createUser_duplicateUsername_throwsException() {
        assertNull(profileRepository.findByEmail("User.Name@example.ch"));
        ProfileEntity testProfile = new ProfileEntity();
        testProfile.setFirstname("User");
        testProfile.setLastname("Name");
        testProfile.setEmail("User.Name@example.ch");
        testProfile.setPhoneNumber("+41 78 123 45 67");
        testProfile.setPassword("OneTwoThreeFour");
        testProfile.setIsSearcher(false);
        ProfileEntity createdProfile = profileService.createUser(testProfile);

        ProfileEntity testProfile2 = new ProfileEntity();
        testProfile2.setFirstname("testName2");
        testProfile2.setLastname("testUsername");
        testProfile2.setEmail(testProfile.getEmail());
        testProfile2.setPhoneNumber("+41 79 234 56 78");
        testProfile2.setPassword("OneTwoThreeFour");
        testProfile2.setIsSearcher(false);

        assertThrows(ResponseStatusException.class, () -> profileService.createUser(testProfile2));
    }

    @Test
    void updateProfile_validInput_success() {
        assertNull(profileRepository.findByEmail("User.Name@example.ch"));
        ProfileEntity testProfile = new ProfileEntity();
        testProfile.setFirstname("User");
        testProfile.setLastname("Name");
        testProfile.setEmail("User.Name@example.ch");
        testProfile.setPhoneNumber("+41 78 123 45 67");
        testProfile.setPassword("OneTwoThreeFour");
        testProfile.setIsSearcher(false);
        ProfileEntity createdProfile = profileService.createUser(testProfile);

        ProfileLifespanDTO profileLifespanDTO = new ProfileLifespanDTO();
        profileLifespanDTO.setIsExperience(true);
        profileLifespanDTO.setText("Nice Experience somewhere");
        profileLifespanDTO.setFromDate(Date.valueOf("2023-03-04"));
        profileLifespanDTO.setToDate(Date.valueOf("2023-04-26"));

        List<ProfileLifespanDTO> profileLifespanEntities = new ArrayList<>();
        profileLifespanEntities.add(profileLifespanDTO);

        ProfilePutDTO testProfile2 = new ProfilePutDTO();
        testProfile2.setFirstname("testName2");
        testProfile2.setLastname("testUsername");
        testProfile2.setBirthday(Date.valueOf("2002-12-26"));
        testProfile2.setPhoneNumber("+41 79 234 56 78");
        testProfile2.setGender("MALE");
        testProfile2.setBiography("A nice dude");
        testProfile2.setFutureFlatmatesDescription("Another nice dude");
        testProfile2.setLifespans(profileLifespanEntities);

        profileService.updateProfile(testProfile.getId(), testProfile2);

        testProfile = profileService.getProfileById(testProfile.getId());

        assertEquals(testProfile2.getFirstname(), testProfile.getFirstname());
        assertEquals(testProfile2.getLastname(), testProfile.getLastname());
        assertEquals(testProfile2.getBirthday(), testProfile.getBirthday());
        assertEquals(testProfile2.getPhoneNumber(), testProfile.getPhoneNumber());
        assertEquals(testProfile2.getGender(), testProfile.getGender());
        assertEquals(testProfile2.getBiography(), testProfile.getBiography());
        assertEquals(testProfile2.getFutureFlatmatesDescription(), testProfile.getFutureFlatmatesDescription());
    }
}
