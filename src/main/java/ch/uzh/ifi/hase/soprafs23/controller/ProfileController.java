package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.Profile;
import ch.uzh.ifi.hase.soprafs23.rest.dto.ProfileGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.ProfilePostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class ProfileController {

  private final ProfileService profileService;

  ProfileController(ProfileService profileService) {
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

  @PostMapping("/profiles")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public ProfileGetDTO createUser(@RequestBody ProfilePostDTO profilePostDTO) {
    // convert API user to internal representation
    Profile profileInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(profilePostDTO);

    // create user
    Profile createdProfile = profileService.createUser(profileInput);
    // convert internal representation of user back to API
    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdProfile);
  }
}
