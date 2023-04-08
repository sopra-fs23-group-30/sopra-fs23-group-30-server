package ch.uzh.ifi.hase.soprafs23.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs23.entity.ApplicationEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ListingEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ProfileEntity;
import ch.uzh.ifi.hase.soprafs23.rest.dto.ApplicantGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.Application.ApplicationGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.Listing.ListingOverviewGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.Profile.ProfileGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.Profile.ProfilePutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.ApplicationService;
import ch.uzh.ifi.hase.soprafs23.service.ListingService;
import ch.uzh.ifi.hase.soprafs23.service.ProfileService;

@RestController
@CrossOrigin
public class ProfilesController {

    private final ProfileService profileService;
    private final ApplicationService applicationService;
    private final ListingService listingService;

    ProfilesController(ProfileService profileService, ApplicationService applicationService, ListingService listingService) {
        this.profileService = profileService;
        this.applicationService = applicationService;
        this.listingService = listingService;
    }

    @GetMapping("/profiles/{id}")
    public ResponseEntity<ProfileGetDTO> getProfileByid(@PathVariable UUID id) {
        ProfileEntity profileEntity = profileService.getProfileById(id);
        ProfileGetDTO profileDTO = DTOMapper.INSTANCE.convertProfileEntityToProfileGetDTO(profileEntity);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(profileDTO);
    }

    @PutMapping("/profiles/{id}")
    public ResponseEntity<?> updateProfileByid(@PathVariable UUID id, @RequestBody ProfilePutDTO updatedProfile) {
        profileService.updateProfile(id, updatedProfile);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }

    @GetMapping("/profiles/{profileId}/applications")
    public ResponseEntity<List<ApplicationGetDTO>> getApplicationsByProfileId(@PathVariable UUID profileId) {
        List<ApplicationEntity> applicationEntities = applicationService.getAllApplicationsByProfileId(profileId);
        List<ApplicationGetDTO> applicationGetDTOs = new ArrayList<>();
        applicationEntities.forEach((applicationEntity) -> applicationGetDTOs.add(DTOMapper.INSTANCE.convertApplicationEntityToApplicationGetDTO(applicationEntity)));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(applicationGetDTOs);
    }

    @GetMapping("/profiles/{profileId}/listings")
    public ResponseEntity<List<ListingOverviewGetDTO>> getListingbyProfileId(@PathVariable UUID profileId) {
        List<ListingEntity> listingEntities = listingService.getListingByProfileId(profileId);
        List<ListingOverviewGetDTO> listingOverviewGetDTOs = new ArrayList<>();
        listingEntities.forEach((listing) -> {
            ListingOverviewGetDTO listingOverviewGetDTO = DTOMapper.INSTANCE.convertListingEntityToListingOverviewGetDTO(listing);
            List<ApplicationEntity> applicationEntities = applicationService.getAllApplicationsByListingId(listing.getId());
            List<ApplicantGetDTO> applicantGetDTOs = new ArrayList<>();

            applicationEntities.forEach((application) -> 
                        applicantGetDTOs.add(DTOMapper.INSTANCE.convertApplicationEntityToApplicantGetDTO(application)));

            listingOverviewGetDTO.setApplicants(applicantGetDTOs);
            listingOverviewGetDTOs.add(listingOverviewGetDTO);
        });
        
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listingOverviewGetDTOs);
    }
}
