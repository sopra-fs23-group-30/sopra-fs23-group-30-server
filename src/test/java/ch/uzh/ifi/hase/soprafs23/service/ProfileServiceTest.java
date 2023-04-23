package ch.uzh.ifi.hase.soprafs23.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs23.config.JwtRequest;
import ch.uzh.ifi.hase.soprafs23.entity.ProfileEntity;
import ch.uzh.ifi.hase.soprafs23.repository.ProfileRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.profile.ProfileLifespanDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.profile.ProfilePutDTO;

class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ProfileLifespanService profileLifespanService;

    @InjectMocks
    private ProfileService profileService;

    private ProfileEntity testProfile;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        testProfile = new ProfileEntity();
        testProfile.setId(new UUID(0, 0));
        testProfile.setFirstname("test");
        testProfile.setLastname("name");
        testProfile.setEmail("test.name@example.com");
        testProfile.setPhoneNumber("+41 79 123 45 67");
        testProfile.setPassword("OneTwoThree");
        testProfile.setIsSearcher(false);

        Mockito.when(profileRepository.save(Mockito.any())).thenReturn(testProfile);
    }

    @Test
    void createUser_validInputs_success() {
        ProfileEntity createdProfile = profileService.createUser(testProfile);

        Mockito.verify(profileRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testProfile.getId(), createdProfile.getId());
        assertEquals(testProfile.getFirstname(), createdProfile.getFirstname());
        assertEquals(testProfile.getLastname(), createdProfile.getLastname());
        assertEquals(testProfile.getEmail(), createdProfile.getEmail());
        assertEquals(testProfile.getPhoneNumber(), createdProfile.getPhoneNumber());
        assertEquals(testProfile.getPassword(), createdProfile.getPassword());
        assertEquals(testProfile.getIsSearcher(), createdProfile.getIsSearcher());
    }

    @Test
    void createUser_duplicateName_throwsException() {
        Mockito.when(profileRepository.findByEmail(Mockito.any())).thenReturn(testProfile);

        assertThrows(ResponseStatusException.class, () -> profileService.createUser(testProfile));
    }

    @Test
    void createUser_invalidEmailFormat_throwsException() {
        testProfile.setEmail("invalidEmailFormat");

        assertThrows(ResponseStatusException.class, () -> profileService.createUser(testProfile));
    }

    @Test
    void createUser_invalidPhoneFormat_throwsException() {
        testProfile.setPhoneNumber("123456789101112131415");

        assertThrows(ResponseStatusException.class, () -> profileService.createUser(testProfile));
    }

    @Test
    void getProfileById_validId_success() {
        Mockito.when(profileRepository.findById(Mockito.any())).thenReturn(java.util.Optional.of(testProfile));

        ProfileEntity foundProfile = profileService.getProfileById(testProfile.getId());

        assertEquals(testProfile.getId(), foundProfile.getId());
        assertEquals(testProfile.getFirstname(), foundProfile.getFirstname());
        assertEquals(testProfile.getLastname(), foundProfile.getLastname());
        assertEquals(testProfile.getEmail(), foundProfile.getEmail());
        assertEquals(testProfile.getPhoneNumber(), foundProfile.getPhoneNumber());
        assertEquals(testProfile.getPassword(), foundProfile.getPassword());
        assertEquals(testProfile.getIsSearcher(), foundProfile.getIsSearcher());
    }

    @Test
    void getProfileById_invalidId_throwsException() {
        Mockito.when(profileRepository.findById(Mockito.any())).thenReturn(java.util.Optional.empty());

        assertThrows(ResponseStatusException.class, () -> profileService.getProfileById(testProfile.getId()));
    }

    @Test
    void updateProfile_validInput_success() {
        Mockito.when(profileRepository.findById(Mockito.any())).thenReturn(java.util.Optional.of(testProfile));

        ProfilePutDTO updatedProfile = new ProfilePutDTO();
        updatedProfile.setFirstname("updated");
        updatedProfile.setLastname("name");
        List<ProfileLifespanDTO> lifespans = new ArrayList<ProfileLifespanDTO>();
        updatedProfile.setLifespans(lifespans);

        profileService.updateProfile(testProfile.getId(), updatedProfile);

        assertEquals("updated", updatedProfile.getFirstname());
        assertEquals("name", updatedProfile.getLastname());
    }

    
    @Test
    void loadUserByUsername_validEmail_success() {
        Mockito.when(profileRepository.findByEmail(Mockito.any())).thenReturn(testProfile);

        UserDetails userDetails = profileService.loadUserByUsername(testProfile.getEmail());

        assertEquals(testProfile.getEmail(), userDetails.getUsername());
        assertEquals(testProfile.getPassword(), userDetails.getPassword());
    }

    @Test
    void loadUserByUsername_InvalidEmail_throwsException() {
        Mockito.when(profileRepository.findById(Mockito.any())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> profileService.loadUserByUsername(testProfile.getEmail()));  
    }

    @Test
    void getProfileBySigninCredentials_validCredentials_success() {
        JwtRequest authenticationRequest = new JwtRequest();
        authenticationRequest.setEmail(testProfile.getEmail());
        authenticationRequest.setPassword(testProfile.getPassword());

        Mockito.when(profileRepository.findByEmail(Mockito.any())).thenReturn(testProfile);

        ProfileEntity profile = profileService.getProfileBySigninCredentials(authenticationRequest);

        assertEquals(testProfile.getId(), profile.getId());
        assertEquals(testProfile.getFirstname(), profile.getFirstname());
        assertEquals(testProfile.getLastname(), profile.getLastname());
        assertEquals(testProfile.getEmail(), profile.getEmail());
        assertEquals(testProfile.getPhoneNumber(), profile.getPhoneNumber());
        assertEquals(testProfile.getPassword(), profile.getPassword());
        assertEquals(testProfile.getIsSearcher(), profile.getIsSearcher());
    }

}
