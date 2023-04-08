package ch.uzh.ifi.hase.soprafs23.service;

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
        newApplication.setState(ApplicationState.PENDING);
        this.applicationRepository.save(newApplication);
        return newApplication;
    }

    public ApplicationEntity getApplicationById(UUID id) {
        Optional<ApplicationEntity> foundApplication = this.applicationRepository.findById(id);

        if (!foundApplication.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Fot the provided application id, no application was found");
        }

        return foundApplication.get();
    }

    public void updateApplication(ApplicationEntity applicationEntity, ApplicationState newState) {
        // todo: implement Regelwerk
        applicationEntity.setState(newState);
        this.applicationRepository.save(applicationEntity);
    }
}
