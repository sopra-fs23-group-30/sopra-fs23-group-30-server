package ch.uzh.ifi.hase.soprafs23.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs23.config.JwtRequest;
import ch.uzh.ifi.hase.soprafs23.entity.ProfileEntity;
import ch.uzh.ifi.hase.soprafs23.repository.ProfileRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.profile.ProfilePutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;

@Service
@Primary
@Transactional
public class ProfileService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(ProfileService.class);

    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    private ProfileLifespanService profileLifespanService;

    @Autowired
    public ProfileService(@Qualifier("profileRepository") ProfileRepository profileRepository,
            ProfileLifespanService profileLifespanService,
            PasswordEncoder passwordEncoder) {
        this.profileRepository = profileRepository;
        this.profileLifespanService = profileLifespanService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ProfileEntity profile = profileRepository.findByEmail(email);
        if (profile == null) {
            log.error("Profile not found");
            throw new UsernameNotFoundException("Profile not found");
        } else {
            log.info("Profile found");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        return new org.springframework.security.core.userdetails.User(profile.getEmail(), profile.getPassword(),
                authorities);
    }

    public ProfileEntity getProfileById(UUID id) {
        Optional<ProfileEntity> foundProfile;
        foundProfile = this.profileRepository.findById(id);

        if (!foundProfile.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "For the provided profile id no profile was found");
        }
        return foundProfile.get();
    }

    public void updateProfile(UUID id, ProfilePutDTO updatedProfile) {
        ProfileEntity existingEntity = getProfileById(id);
        ProfileEntity updatedEntity = DTOMapper.INSTANCE.convertProfilePutDTOToProfileEntity(updatedProfile);
        updatedEntity.setId(existingEntity.getId());
        updatedEntity.setEmail(existingEntity.getEmail());
        updatedEntity.setPassword(existingEntity.getPassword());
        updatedEntity.setIsSearcher(existingEntity.getIsSearcher());
        this.profileRepository.save(updatedEntity);

        profileLifespanService.updateProfileLifespans(id, updatedProfile.getLifespans());
    }

    public ProfileEntity getProfileBySigninCredentials(JwtRequest authenticationRequest) {
        return this.profileRepository.findByEmail(authenticationRequest.getEmail());
    }

    public ProfileEntity createUser(ProfileEntity newProfile) {
        validateRegistration(newProfile);
        newProfile.setPassword(passwordEncoder.encode(newProfile.getPassword()));
        newProfile = profileRepository.save(newProfile);
        profileRepository.flush();
        log.debug("Created Information for User: {}", newProfile);
        return newProfile;
    }

    /**
     * @param profileToBeCreated
     * @throws org.springframework.web.server.ResponseStatusException
     * @see ProfileEntity
     */
    private void validateRegistration(ProfileEntity profileToBeCreated) {
        ProfileEntity profileByEmail = profileRepository.findByEmail(profileToBeCreated.getEmail());

        String errorMessage = "";
        boolean isRegistrationValid = true;

        if (profileByEmail != null) {
            isRegistrationValid = false;
            errorMessage += "The provided e-mail is not unique. ";
        }
        if(profileToBeCreated.getEmail() == null || profileToBeCreated.getEmail().isEmpty()) {
            errorMessage += "The provided e-mail is empty. ";
        } else if (!isEmailFormatValid(profileToBeCreated.getEmail())) {
            isRegistrationValid = false;
            errorMessage += "The provided e-mail is not valid. ";
        }
        if(profileToBeCreated.getPhoneNumber() == null || profileToBeCreated.getPhoneNumber().isEmpty()) {
            isRegistrationValid = false;
            errorMessage += "The provided phone number is empty. ";
        } else if (!isPhoneNumberValid(profileToBeCreated.getPhoneNumber())) {
            isRegistrationValid = false;
            errorMessage += "The provided phone number is not valid. Please enter the phone number exactly in this format: +41 XX XXX XX XX. ";
        }
        if(profileToBeCreated.getFirstname() == null || profileToBeCreated.getFirstname().isEmpty()) {
            isRegistrationValid = false;
            errorMessage += "The provided firstname is empty. ";
        }
        if(profileToBeCreated.getLastname() == null || profileToBeCreated.getLastname().isEmpty()) {
            isRegistrationValid = false;
            errorMessage += "The provided lastname is empty. ";
        }
        if(profileToBeCreated.getPassword() == null || profileToBeCreated.getPassword().isEmpty()) {
            isRegistrationValid = false;
            errorMessage += "The provided password is empty. ";
        }
        if(!isRegistrationValid) {
            errorMessage += "Therefore, the profile could not be created.";
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }
    }

    private boolean isEmailFormatValid(String eMail) {
        return eMail.matches("^(.+)@(.+)$");
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        return (phoneNumber.matches("(\\+41)\s(\\d{2})\s(\\d{3})\\s(\\d{2})\\s(\\d{2})"));
    }
}