package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.ProfileStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Profile;
import ch.uzh.ifi.hase.soprafs23.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

public class ProfileServiceTest {

  @Mock
  private ProfileRepository profileRepository;

  @InjectMocks
  private ProfileService profileService;

  private Profile testProfile;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    // given
    testProfile = new Profile();
    testProfile.setId(1L);
    testProfile.setFirstname("testName");
    testProfile.setLastname("testUsername");

    // when -> any object is being save in the userRepository -> return the dummy
    // testUser
    Mockito.when(profileRepository.save(Mockito.any())).thenReturn(testProfile);
  }

  @Test
  public void createUser_validInputs_success() {
    // when -> any object is being save in the userRepository -> return the dummy
    // testUser
    Profile createdProfile = profileService.createUser(testProfile);

    // then
    Mockito.verify(profileRepository, Mockito.times(1)).save(Mockito.any());

    assertEquals(testProfile.getId(), createdProfile.getId());
    assertEquals(testProfile.getFirstname(), createdProfile.getFirstname());
    assertEquals(testProfile.getLastname(), createdProfile.getLastname());
    assertNotNull(createdProfile.getToken());
    assertEquals(ProfileStatus.OFFLINE, createdProfile.getStatus());
  }

  @Test
  public void createUser_duplicateName_throwsException() {
    // given -> a first user has already been created
    profileService.createUser(testProfile);

    // when -> setup additional mocks for UserRepository
    Mockito.when(profileRepository.findByeMail(Mockito.any())).thenReturn(testProfile);

    // then -> attempt to create second user with same user -> check that an error
    // is thrown
    assertThrows(ResponseStatusException.class, () -> profileService.createUser(testProfile));
  }

  @Test
  public void createUser_duplicateInputs_throwsException() {
    // given -> a first user has already been created
    profileService.createUser(testProfile);

    // when -> setup additional mocks for UserRepository
    Mockito.when(profileRepository.findByeMail(Mockito.any())).thenReturn(testProfile);

    // then -> attempt to create second user with same user -> check that an error
    // is thrown
    assertThrows(ResponseStatusException.class, () -> profileService.createUser(testProfile));
  }

}
