// package ch.uzh.ifi.hase.soprafs23.service;

// import ch.uzh.ifi.hase.soprafs23.constant.ProfileStatus;
// import ch.uzh.ifi.hase.soprafs23.entity.Profile;
// import ch.uzh.ifi.hase.soprafs23.repository.ProfileRepository;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.Mockito;
// import org.mockito.MockitoAnnotations;
// import org.springframework.web.server.ResponseStatusException;

// import static org.junit.jupiter.api.Assertions.*;

// public class ProfileServiceTest {

//     @Mock
//     private ProfileRepository profileRepository;

//     @InjectMocks
//     private ProfileService profileService;

//     private Profile testProfile;

//     @BeforeEach
//     public void setup() {
//         MockitoAnnotations.openMocks(this);

//         testProfile = new Profile();
//         testProfile.setId(1L);
//         testProfile.setFirstname("test");
//         testProfile.setLastname("name");
//         testProfile.setEmail("test.name@example.com");
//         testProfile.setPhoneNumber("0791234567");
//         testProfile.setPassword("OneTwoThree");
//         testProfile.setSearcher(false);

//         Mockito.when(profileRepository.save(Mockito.any())).thenReturn(testProfile);
//     }

//     @Test
//     public void createUser_validInputs_success() {
//         Profile createdProfile = profileService.createUser(testProfile);

//         Mockito.verify(profileRepository, Mockito.times(1)).save(Mockito.any());

//         assertEquals(testProfile.getId(), createdProfile.getId());
//         assertEquals(testProfile.getFirstname(), createdProfile.getFirstname());
//         assertEquals(testProfile.getLastname(), createdProfile.getLastname());
//         assertEquals(testProfile.getEmail(), createdProfile.getEmail());
//         assertEquals(testProfile.getPhoneNumber(), createdProfile.getPhoneNumber());
//         assertEquals(testProfile.getPassword(), createdProfile.getPassword());
//         assertEquals(testProfile.isSearcher(), createdProfile.isSearcher());
//         assertNotNull(createdProfile.getToken());
//         assertEquals(ProfileStatus.OFFLINE, createdProfile.getStatus());
//     }

//     @Test
//     public void createUser_duplicateName_throwsException() {
//         profileService.createUser(testProfile);

//         Mockito.when(profileRepository.findByEmail(Mockito.any())).thenReturn(testProfile);

//         assertThrows(ResponseStatusException.class, () -> profileService.createUser(testProfile));
//     }

//     @Test
//     public void createUser_duplicateInputs_throwsException() {
//         profileService.createUser(testProfile);

//         Mockito.when(profileRepository.findByEmail(Mockito.any())).thenReturn(testProfile);

//         assertThrows(ResponseStatusException.class, () -> profileService.createUser(testProfile));
//     }

// }
