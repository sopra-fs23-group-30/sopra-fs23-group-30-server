package ch.uzh.ifi.hase.soprafs23.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs23.entity.ProfileEntity;
import ch.uzh.ifi.hase.soprafs23.rest.dto.ProfileGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.ProfilePutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.ProfileService;

@RestController
@CrossOrigin
public class ProfilesController {

    private final ProfileService profileService;

    ProfilesController(ProfileService profileService) {
        this.profileService = profileService;
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
}
