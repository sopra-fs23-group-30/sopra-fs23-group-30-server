package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.ProfileStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Profile;
import ch.uzh.ifi.hase.soprafs23.repository.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class ProfileService {

  private final Logger log = LoggerFactory.getLogger(ProfileService.class);

  private final ProfileRepository profileRepository;

  @Autowired
  public ProfileService(@Qualifier("userRepository") ProfileRepository profileRepository) {
    this.profileRepository = profileRepository;
  }

  public List<Profile> getUsers() {
    return this.profileRepository.findAll();
  }

  public Profile createUser(Profile newProfile) {
    newProfile.setToken(UUID.randomUUID().toString());
    newProfile.setStatus(ProfileStatus.OFFLINE);
    checkIfUserExists(newProfile);
    // saves the given entity but data is only persisted in the database once
    // flush() is called
    newProfile = profileRepository.save(newProfile);
    profileRepository.flush();

    log.debug("Created Information for User: {}", newProfile);
    return newProfile;
  }

  /**
   * This is a helper method that will check the uniqueness criteria of the
   * username and the name
   * defined in the User entity. The method will do nothing if the input is unique
   * and throw an error otherwise.
   *
   * @param profileToBeCreated
   * @throws org.springframework.web.server.ResponseStatusException
   * @see Profile
   */
  private void checkIfUserExists(Profile profileToBeCreated) {
    Profile profileByUsername = profileRepository.findByUsername(profileToBeCreated.getUsername());
    Profile profileByName = profileRepository.findByName(profileToBeCreated.getName());

    String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
    if (profileByUsername != null && profileByName != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format(baseErrorMessage, "username and the name", "are"));
    } else if (profileByUsername != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "username", "is"));
    } else if (profileByName != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "name", "is"));
    }
  }
}
