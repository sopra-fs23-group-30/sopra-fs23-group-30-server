package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.Profile;
import ch.uzh.ifi.hase.soprafs23.rest.dto.ProfileGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.RegisterProfileDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class AuthenticationController {

    private final ProfileService profileService;

    AuthenticationController(ProfileService profileService) {
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

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<Map<String, String>> createUser(@RequestBody RegisterProfileDTO registerProfileDTO) {
        // convert API user to internal representation
        Profile profileInput = DTOMapper.INSTANCE.convertRegisterProfileDTOtoEntity(registerProfileDTO);
        // create user
        Profile createdProfile = profileService.createUser(profileInput);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", createdProfile.getToken());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseBody);
    }
}
