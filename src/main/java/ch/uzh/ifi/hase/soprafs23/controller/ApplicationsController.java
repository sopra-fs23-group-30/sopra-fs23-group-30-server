package ch.uzh.ifi.hase.soprafs23.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs23.constant.ApplicationState;
import ch.uzh.ifi.hase.soprafs23.entity.ApplicationEntity;
import ch.uzh.ifi.hase.soprafs23.rest.dto.application.ApplicationPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.application.ApplicationPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.ApplicationService;
import ch.uzh.ifi.hase.soprafs23.service.ListingService;
import ch.uzh.ifi.hase.soprafs23.service.ProfileService;

@RestController
@CrossOrigin
public class ApplicationsController {

        private final ApplicationService applicationService;
        private ProfileService profileService;
        private ListingService listingService;
        private WebSocketController webSocketController;

        ApplicationsController(ApplicationService applicationService, ProfileService profileService,
                        ListingService listingService, WebSocketController webSocketController) {
                this.applicationService = applicationService;
                this.profileService = profileService;
                this.listingService = listingService;
                this.webSocketController = webSocketController;
        }

        @PostMapping("/applications")
        public ResponseEntity<ApplicationEntity> createApplication(@RequestBody ApplicationPostDTO applicationDTO) {

                ApplicationEntity applicationEntity = DTOMapper.INSTANCE
                                .convertApplicationPostDTOToApplicationEntity(applicationDTO);
                applicationEntity.setApplicant(profileService.getProfileById(applicationDTO.getApplicantId()));
                applicationEntity.setListing(listingService.getListingById(applicationDTO.getListingId()));

                ApplicationEntity createdApplication = applicationService.createApplication(applicationEntity);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(createdApplication);
        }

        @PutMapping("/applications/{id}")
        public ResponseEntity<Object> updateApplicationState(@PathVariable UUID id,
                        @RequestBody ApplicationPutDTO applicationDTO) {
                ApplicationEntity applicationEntity = applicationService.getApplicationById(id);

                ApplicationEntity updatedApplication = applicationService.updateApplication(applicationEntity, applicationDTO.getNewState());
                
                if(applicationDTO.getNewState() == ApplicationState.MOVEIN) {
                        applicationService.declineAllOtherApplicationsByListingId(
                                        applicationEntity.getListing().getId(), applicationEntity.getId());
                        applicationService.declineAllOtherApplicationsByProfileId(
                                        applicationEntity.getApplicant().getId(), applicationEntity.getId());
                }

                updatedApplication.setListing(listingService.getListingById(applicationEntity.getListing().getId()));
                webSocketController.applicationStateUpdatedToUser(updatedApplication.getApplicant().getId(), updatedApplication);
                webSocketController.applicationStateUpdatedToUser(updatedApplication.getListing().getLister().getId(), updatedApplication);

                return ResponseEntity
                                .status(HttpStatus.NO_CONTENT)
                                .body(null);
        }
}
