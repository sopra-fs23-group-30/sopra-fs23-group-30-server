package ch.uzh.ifi.hase.soprafs23.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs23.constant.ApplicationState;
import ch.uzh.ifi.hase.soprafs23.entity.ApplicationEntity;
import ch.uzh.ifi.hase.soprafs23.repository.ApplicationRepository;

@Service
@Primary
@Transactional
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationService(@Qualifier("applicationRepository") ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public ApplicationEntity createApplication(ApplicationEntity newApplication) {
        List<ApplicationEntity> applicationsByListing = applicationRepository
                .findByListingId(newApplication.getListing().getId());

        applicationsByListing.forEach(application -> {
            if (application.getApplicant().getId() == newApplication.getApplicant().getId()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "For the provided listing and user id there allready exists an application.");
            }
        });

        newApplication.setState(ApplicationState.PENDING);
        this.applicationRepository.save(newApplication);
        return newApplication;
    }

    public ApplicationEntity getApplicationById(UUID id) {
        Optional<ApplicationEntity> foundApplication = this.applicationRepository.findById(id);

        if (!foundApplication.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "For the provided application id no application was found");
        }

        return foundApplication.get();
    }

    public void updateApplication(ApplicationEntity applicationEntity, ApplicationState newState) {
        if (!applicationEntity.getState().isTransitionValid(newState)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    String.format("Updating a %s state into a %s state is not allowed.",
                            applicationEntity.getState().toString(), newState.toString()));
        }
        applicationEntity.setState(newState);
        if (newState == ApplicationState.MOVEIN) {
            applicationEntity.setInventoryId(UUID.randomUUID());
        }
        this.applicationRepository.save(applicationEntity);
    }

    public List<ApplicationEntity> getAllApplicationsByProfileId(UUID id) {
        return this.applicationRepository.findByApplicantId(id);
    }

    public List<ApplicationEntity> getAllApplicationsByListingId(UUID id) {
        return this.applicationRepository.findByListingId(id);
    }

    public void declineAllOtherApplicationsByListingId(UUID id, UUID idOfAccepted) {
        List<ApplicationEntity> entities = this.getAllApplicationsByListingId(id);
        for (ApplicationEntity applicationEntity : entities) {
            if (applicationEntity.getId() != idOfAccepted) {
                applicationEntity.setState(ApplicationState.DECLINED);
                this.applicationRepository.save(applicationEntity);
            }
        }
    }
}
