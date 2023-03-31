package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.ProfileStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Profile;
import ch.uzh.ifi.hase.soprafs23.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

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
    public void createUser_validInputs_success() {
        // given
        assertNull(profileRepository.findByeMail("User.Name@example.ch"));

        Profile testProfile = new Profile();
        testProfile.setFirstname("User");
        testProfile.setLastname("Name");
        testProfile.seteMail("User.Name@example.ch");
        testProfile.setPhoneNumber("0781234567");
        testProfile.setPassword("OneTwoThreeFour");
        testProfile.setSearcher(false);

        // when
        Profile createdProfile = profileService.createUser(testProfile);

        // then
        assertEquals(testProfile.getId(), createdProfile.getId());
        assertEquals(testProfile.getFirstname(), createdProfile.getFirstname());
        assertEquals(testProfile.getLastname(), createdProfile.getLastname());
        assertEquals(testProfile.geteMail(), createdProfile.geteMail());
        assertEquals(testProfile.getPhoneNumber(), createdProfile.getPhoneNumber());
        assertEquals(testProfile.getPassword(), createdProfile.getPassword());
        assertEquals(testProfile.isSearcher(), createdProfile.isSearcher());
        assertNotNull(createdProfile.getToken());
        assertEquals(ProfileStatus.OFFLINE, createdProfile.getStatus());
    }

    @Test
    public void createUser_duplicateUsername_throwsException() {
        assertNull(profileRepository.findByeMail("User.Name@example.ch"));

        Profile testProfile = new Profile();
        testProfile.setFirstname("User");
        testProfile.setLastname("Name");
        testProfile.seteMail("User.Name@example.ch");
        testProfile.setPhoneNumber("0781234567");
        testProfile.setPassword("OneTwoThreeFour");
        testProfile.setSearcher(false);
        Profile createdProfile = profileService.createUser(testProfile);

        // attempt to create second user with same username
        Profile testProfile2 = new Profile();

        // change the name but forget about the username
        testProfile2.setFirstname("testName2");
        testProfile2.setLastname("testUsername");
        testProfile2.seteMail(testProfile.geteMail());
        testProfile2.setPhoneNumber("0792345678");
        testProfile2.setPassword("OneTwoThreeFour");
        testProfile2.setSearcher(false);

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> profileService.createUser(testProfile2));
    }
}
