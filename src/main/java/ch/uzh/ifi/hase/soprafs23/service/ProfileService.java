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

@Service
@Transactional
public class ProfileService {

    private final Logger log = LoggerFactory.getLogger(ProfileService.class);

    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileService(@Qualifier("profileRepository") ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public List<Profile> getUsers() {
        return this.profileRepository.findAll();
    }

    public Profile createUser(Profile newProfile) {
        newProfile.setToken(UUID.randomUUID().toString());
        newProfile.setStatus(ProfileStatus.OFFLINE);
        validateRegistration(newProfile);

        newProfile = profileRepository.save(newProfile);
        profileRepository.flush();

        log.debug("Created Information for User: {}", newProfile);
        return newProfile;
    }

    /**
     * @param profileToBeCreated
     * @throws org.springframework.web.server.ResponseStatusException
     * @see Profile
     */
    private void validateRegistration(Profile profileToBeCreated) {
        Profile profileByEmail = profileRepository.findByemail(profileToBeCreated.getEmail());

        if (profileByEmail != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The provided e-mail is not unique. Therefore, the profile could not be registered!");
        }
        else if (!isEmailFormatValid(profileToBeCreated.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The provided e-mail is not valid. Therefore, the profile could not be registered!");
        }
        else if (!isPhoneNumberValid(profileToBeCreated.getPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The provided phone number is not valid. Therefore, the profile could not be registered!");
        }
    }

    private boolean isEmailFormatValid(String eMail) {
        return eMail.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$");
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        return (phoneNumber.matches("[1-9]{0,1}[0-9]{10}"));
    }
}
