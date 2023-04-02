package ch.uzh.ifi.hase.soprafs23.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs23.entity.Profile;
import ch.uzh.ifi.hase.soprafs23.rest.dto.ProfileGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.ProfileService;

@RestController
@CrossOrigin
public class ProfilesController {
        
    private final ProfileService profileService;

    ProfilesController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/profiles")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ProfileGetDTO> getAllUsers() {
        // fetch all users in the internal representation
        List<Profile> profiles = profileService.getUsers();
        List<ProfileGetDTO> profileGetDTOS = new ArrayList<>();

        // convert each user to the API representation
        for (Profile profile : profiles) {
            profileGetDTOS.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(profile));
        }
        return profileGetDTOS;
    }
}