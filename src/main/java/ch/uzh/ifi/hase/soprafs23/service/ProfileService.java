package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.config.JwtRequest;
import ch.uzh.ifi.hase.soprafs23.constant.ProfileStatus;
import ch.uzh.ifi.hase.soprafs23.entity.ProfileEntity;
import ch.uzh.ifi.hase.soprafs23.repository.ProfileRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.ProfilePutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Primary
@Transactional
public class ProfileService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(ProfileService.class);

    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ProfileService(@Qualifier("profileRepository") ProfileRepository profileRepository,
            PasswordEncoder passwordEncoder) {
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // No typo, overriden class is called loadUserByUsername from UserDetailsService
    // (Spring Security)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ProfileEntity profile = profileRepository.findByEmail(email);
        if (profile == null) {
            log.error("Profile not found");
            throw new UsernameNotFoundException("Profile not found");
        } else {
            log.info("Profile found");
        }
        // TODO: Check authorities
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        return new org.springframework.security.core.userdetails.User(profile.getEmail(), profile.getPassword(),
                authorities);
    }

    public List<ProfileEntity> getUsers() {
        return this.profileRepository.findAll();
    }

    public ProfileEntity getProfileById(UUID id) {
        Optional<ProfileEntity> foundProfile;
        foundProfile = this.profileRepository.findById(id);

        if (!foundProfile.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "For the provided profile id, no profile was found");
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

        if (profileByEmail != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The provided e-mail is not unique. Therefore, the profile could not be registered!");
        } else if (!isEmailFormatValid(profileToBeCreated.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The provided e-mail is not valid. Therefore, the profile could not be registered!");
        } else if (!isPhoneNumberValid(profileToBeCreated.getPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The provided phone number is not valid. Therefore, the profile could not be registered!");
        }
    }

    private boolean isEmailFormatValid(String eMail) {
        return eMail.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$");
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        return (phoneNumber.matches("[0-9]{0,1}[0-9]{10}"));
    }
}
