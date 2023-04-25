package ch.uzh.ifi.hase.soprafs23.service;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ch.uzh.ifi.hase.soprafs23.entity.ProfileEntity;
import ch.uzh.ifi.hase.soprafs23.repository.ProfileLifespanRepository;
import ch.uzh.ifi.hase.soprafs23.repository.ProfileRepository;

public class ProfileLifespanServiceTest {

    @Mock
    private ProfileLifespanRepository profileLifespanRepository;

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileLifespanService profileLifespanService;

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

}
