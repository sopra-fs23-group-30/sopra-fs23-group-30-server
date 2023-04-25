package ch.uzh.ifi.hase.soprafs23.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs23.constant.ApplicationState;
import ch.uzh.ifi.hase.soprafs23.entity.ApplicationEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ListingEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ProfileEntity;
import ch.uzh.ifi.hase.soprafs23.repository.ApplicationRepository;

class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private ApplicationService applicationService;

    private ApplicationEntity testApplication;
    private ProfileEntity testProfile;
    private ListingEntity testListing;

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

        testListing = new ListingEntity();
        testListing.setId(new UUID(0, 0));
        testListing.setCreationDate(LocalDateTime.now());

        testApplication = new ApplicationEntity();
        testApplication.setId(new UUID(0, 0));
        testApplication.setApplicant(testProfile);
        testApplication.setListing(testListing);
        testApplication.setState(ApplicationState.PENDING);

        Mockito.when(applicationRepository.save(Mockito.any())).thenReturn(testApplication);
    }


    @Test
    void createApplication_validInputs_success() {
        ApplicationEntity createdApplication = applicationService.createApplication(testApplication);

        assertEquals(testApplication.getId(), createdApplication.getId());
        assertEquals(testApplication.getApplicant(), createdApplication.getApplicant());
        assertEquals(testApplication.getListing(), createdApplication.getListing());
        assertEquals(testApplication.getState(), createdApplication.getState());
    }

    @Test
    void createApplication_applyingTwiceOnTheSameListing_throwsException() {
        List<ApplicationEntity> allApplicants = new ArrayList<ApplicationEntity>();
        allApplicants.add(testApplication);

        Mockito.when(applicationRepository.findByListingId(Mockito.any())).thenReturn(allApplicants);

        assertThrows(ResponseStatusException.class, () -> applicationService.createApplication(testApplication));
    }

    @Test
    void getApplicationById_validInput_thenSuccess() {
        Mockito.when(applicationRepository.findById(Mockito.any())).thenReturn(java.util.Optional.of(testApplication));
        
        ApplicationEntity foundApplication = applicationService.getApplicationById(testApplication.getId());
        
        assertEquals(testApplication.getId(), foundApplication.getId());
        assertEquals(testApplication.getApplicant(), foundApplication.getApplicant());
        assertEquals(testApplication.getListing(), foundApplication.getListing());
        assertEquals(testApplication.getState(), foundApplication.getState());
    }

    @Test
    void getApplicationById_invalidInput_thenThrowsException() {
        Mockito.when(applicationRepository.findById(Mockito.any())).thenReturn(java.util.Optional.empty());

        UUID applicationId = testApplication.getId();
        
        assertThrows(ResponseStatusException.class, () -> applicationService.getApplicationById(applicationId));
    }
    
    @Test
    void updateApplication_validInput_success() {
        Mockito.when(applicationRepository.findById(Mockito.any())).thenReturn(java.util.Optional.of(testApplication));

        applicationService.updateApplication(testApplication, ApplicationState.DECLINED);

        assertEquals(ApplicationState.DECLINED, testApplication.getState());
    }
    
    @Test
    void updateApplication_invalidInput_success() {
        Mockito.when(applicationRepository.findById(Mockito.any())).thenReturn(java.util.Optional.of(testApplication));

        assertThrows(ResponseStatusException.class, () -> applicationService.updateApplication(testApplication, ApplicationState.PENDING));
    }

    @Test
    void getAllApplicationsByProfileId_validInput_success() {
        List<ApplicationEntity> applications = new ArrayList<>();
        applications.add(testApplication);

        Mockito.when(applicationRepository.findByApplicantId(testApplication.getApplicant().getId())).thenReturn(applications);

        List<ApplicationEntity> recieved = applicationService.getAllApplicationsByProfileId(testApplication.getApplicant().getId());
        ApplicationEntity foundApplication = recieved.get(0);

        assertEquals(testApplication.getId(), foundApplication.getId());
        assertEquals(testApplication.getApplicant(), foundApplication.getApplicant());
        assertEquals(testApplication.getListing(), foundApplication.getListing());
        assertEquals(testApplication.getState(), foundApplication.getState());
    }

    @Test
    void getAllApplicationsByProfileId_invalidInput_success() {
        Mockito.when(applicationRepository.findByApplicantId(testApplication.getApplicant().getId())).thenReturn(new ArrayList<>());

        List<ApplicationEntity> recieved = applicationService.getAllApplicationsByProfileId(testApplication.getApplicant().getId());
        
        assertEquals(0, recieved.size());
    }

    @Test
    void getAllApplicationsByListingId_validInput_success() {
        List<ApplicationEntity> applications = new ArrayList<>();
        applications.add(testApplication);

        Mockito.when(applicationRepository.findByApplicantId(testApplication.getListing().getId())).thenReturn(applications);

        List<ApplicationEntity> recieved = applicationService.getAllApplicationsByProfileId(testApplication.getListing().getId());
        ApplicationEntity foundApplication = recieved.get(0);

        assertEquals(testApplication.getId(), foundApplication.getId());
        assertEquals(testApplication.getApplicant(), foundApplication.getApplicant());
        assertEquals(testApplication.getListing(), foundApplication.getListing());
        assertEquals(testApplication.getState(), foundApplication.getState());
    }

    @Test
    void getAllApplicationsByListingId_invalidInput_success() {
        Mockito.when(applicationRepository.findByApplicantId(testApplication.getApplicant().getId())).thenReturn(new ArrayList<>());
        
        List<ApplicationEntity> recieved = applicationService.getAllApplicationsByProfileId(testApplication.getApplicant().getId());
        
        assertEquals(0, recieved.size());
    }
}
