package ch.uzh.ifi.hase.soprafs23.service;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs23.entity.ListingEntity;
import ch.uzh.ifi.hase.soprafs23.repository.ListingRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.Listing.ListingPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;

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

    public ListingEntity getListingById(UUID id) {
        Optional<ListingEntity> foundListing = this.listingRepository.findById(id);

        if (!foundListing.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Fot the provided listing id, no listing was found");
        }

        return foundListing.get();
    }

    public void deleteListingById(UUID id) {

        Optional<ListingEntity> foundListing = this.listingRepository.findById(id);

        if (!foundListing.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Fot the provided listing id, no listing was found");
        }

        listingRepository.delete(foundListing.get());
    }

    public void updateListing(UUID id, ListingPutDTO updatedListing) {
        ListingEntity existingEntity = getListingById(id);
        ListingEntity updatedEntity = DTOMapper.INSTANCE.convertListingPostDTOToListingEntity(updatedListing);
        updatedEntity.setId(existingEntity.getId());
        updatedEntity.setCreationDate(existingEntity.getCreationDate());
        updatedEntity.setLister(existingEntity.getLister());
        this.listingRepository.save(updatedEntity);
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
