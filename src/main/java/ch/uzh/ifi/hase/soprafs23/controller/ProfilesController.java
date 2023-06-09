package ch.uzh.ifi.hase.soprafs23.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.uzh.ifi.hase.soprafs23.entity.ApplicationEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ListingEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ProfileEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ProfileLifespanEntity;
import ch.uzh.ifi.hase.soprafs23.rest.dto.ApplicantGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.application.ApplicationGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.listing.ListingOverviewGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.profile.ProfileGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.profile.ProfileLifespanDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.profile.ProfilePutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.ApplicationService;
import ch.uzh.ifi.hase.soprafs23.service.BlobUploaderService;
import ch.uzh.ifi.hase.soprafs23.service.ListingService;
import ch.uzh.ifi.hase.soprafs23.service.ProfileLifespanService;
import ch.uzh.ifi.hase.soprafs23.service.ProfileService;

@RestController
@CrossOrigin
public class ProfilesController {

        private final ProfileService profileService;
        private final ApplicationService applicationService;
        private final ListingService listingService;
        private ProfileLifespanService profileLifespanService;
        private BlobUploaderService blobUploaderService;

        ProfilesController(ProfileService profileService, ApplicationService applicationService,
                        ListingService listingService, ProfileLifespanService profileLifespanService,
                        BlobUploaderService blobUploaderService) {
                this.profileService = profileService;
                this.applicationService = applicationService;
                this.listingService = listingService;
                this.profileLifespanService = profileLifespanService;
                this.blobUploaderService = blobUploaderService;
        }

        @GetMapping("/profiles/{id}")
        public ResponseEntity<ProfileGetDTO> getProfileByid(@PathVariable UUID id) {
                ProfileEntity profileEntity = profileService.getProfileById(id);
                ProfileGetDTO profileDTO = DTOMapper.INSTANCE.convertProfileEntityToProfileGetDTO(profileEntity);
                List<ProfileLifespanEntity> lifespanEntities = profileLifespanService.getAllLifespansByProfileId(id);
                List<ProfileLifespanDTO> lifespanDtos = new ArrayList<>();
                lifespanEntities.forEach(lifespanEntity -> lifespanDtos
                                .add(DTOMapper.INSTANCE.convertLifespanEntityToLifespanDTO(lifespanEntity)));
                profileDTO.setLifespans(lifespanDtos);

                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(profileDTO);
        }

        @PutMapping(value = "/profiles/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE,
                        MediaType.MULTIPART_FORM_DATA_VALUE })
        public ResponseEntity<Object> updateProfileByid(@PathVariable UUID id,
                        @RequestPart("body") String updatedProfile,
                        @RequestPart("file") MultipartFile file,
                        @RequestPart("document") MultipartFile document) throws IOException {

                ProfilePutDTO updateProfile = new ProfilePutDTO();
                try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        updateProfile = objectMapper.readValue(updatedProfile, ProfilePutDTO.class);
                } catch (IOException e) {
                        return ResponseEntity
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(null);
                }

                try {
                        String originalFilename = file.getOriginalFilename();
                        if ("deleted".equals(originalFilename))
                                updateProfile.setProfilePictureURL(null);

                        else if (!"unchanged".equals(originalFilename)) {
                                String blobURL = blobUploaderService.upload(file, "profilepictures", id.toString());
                                updateProfile.setProfilePictureURL(blobURL);
                        }
                } catch (NullPointerException e) {
                        return ResponseEntity
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(null);
                }

                try {
                        String originalDocumentFilename = document.getOriginalFilename();
                        if ("deleted".equals(originalDocumentFilename))
                                updateProfile.setDocumentURL(null);

                        else if (!"unchanged".equals(originalDocumentFilename)) {
                                String blobURL = blobUploaderService.upload(document, "debtcollectionregisters",
                                                id.toString());
                                updateProfile.setDocumentURL(blobURL);
                        }
                } catch (NullPointerException e) {
                        return ResponseEntity
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(null);
                }

                profileService.updateProfile(id, updateProfile);

                return ResponseEntity
                                .status(HttpStatus.NO_CONTENT)
                                .body(null);
        }

        @GetMapping("/profiles/{profileId}/applications")
        public ResponseEntity<List<ApplicationGetDTO>> getApplicationsByProfileId(@PathVariable UUID profileId) {
                List<ApplicationEntity> applicationEntities = applicationService
                                .getAllApplicationsByProfileId(profileId);
                List<ApplicationGetDTO> applicationGetDTOs = new ArrayList<>();
                applicationEntities.forEach(applicationEntity -> applicationGetDTOs
                                .add(DTOMapper.INSTANCE
                                                .convertApplicationEntityToApplicationGetDTO(applicationEntity)));
                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(applicationGetDTOs);
        }

        @GetMapping("/profiles/{profileId}/listings")
        public ResponseEntity<List<ListingOverviewGetDTO>> getListingbyProfileId(@PathVariable UUID profileId) {
                List<ListingEntity> listingEntities = listingService.getListingByProfileId(profileId);
                List<ListingOverviewGetDTO> listingOverviewGetDTOs = new ArrayList<>();
                listingEntities.forEach(listing -> {
                        ListingOverviewGetDTO listingOverviewGetDTO = DTOMapper.INSTANCE
                                        .convertListingEntityToListingOverviewGetDTO(listing);
                        List<ApplicationEntity> applicationEntities = applicationService
                                        .getAllApplicationsByListingId(listing.getId());
                        List<ApplicantGetDTO> applicantGetDTOs = new ArrayList<>();

                        applicationEntities.forEach(application -> applicantGetDTOs
                                        .add(DTOMapper.INSTANCE
                                                        .convertApplicationEntityToApplicantGetDTO(application)));

                        listingOverviewGetDTO.setApplicants(applicantGetDTOs);
                        listingOverviewGetDTOs.add(listingOverviewGetDTO);
                });

                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(listingOverviewGetDTOs);
        }
}
