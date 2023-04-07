package ch.uzh.ifi.hase.soprafs23.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs23.entity.ListingEntity;
import ch.uzh.ifi.hase.soprafs23.repository.ListingRepository;

@Service
@Primary
@Transactional
public class ListingService {

    private final Logger log = LoggerFactory.getLogger(ListingService.class);

    private final ListingRepository listingRepository;

    @Autowired
    public ListingService(@Qualifier("listingRepository") ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    public ListingEntity createListing(ListingEntity newListing) {
        validateListing(newListing);
        this.listingRepository.save(newListing);
        return newListing;
    }

    /**
     * @param listingToBeCreated
     * @throws org.springframework.web.server.ResponseStatusException
     * @see ListingEntity
     */
    private void validateListing(ListingEntity listingToBeCreated) {
        // ProfileEntity profileByEmail =
        // profileRepository.findByEmail(profileToBeCreated.getEmail());

        // if (profileByEmail != null) {
        // throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
        // "The provided e-mail is not unique. Therefore, the profile could not be
        // registered!");
        // } else if (!isEmailFormatValid(profileToBeCreated.getEmail())) {
        // throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
        // "The provided e-mail is not valid. Therefore, the profile could not be
        // registered!");
        // } else if (!isPhoneNumberValid(profileToBeCreated.getPhoneNumber())) {
        // throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
        // "The provided phone number is not valid. Therefore, the profile could not be
        // registered!");
        // }
    }

    private boolean isEmailFormatValid(String eMail) {
        return eMail.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$");
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        return (phoneNumber.matches("[0-9]{0,1}[0-9]{10}"));
    }
}