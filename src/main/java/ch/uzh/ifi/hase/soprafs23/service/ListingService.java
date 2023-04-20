package ch.uzh.ifi.hase.soprafs23.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs23.entity.ListingEntity;
import ch.uzh.ifi.hase.soprafs23.repository.ListingRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.listing.ListingPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;

@Service
@Primary
@Transactional
public class ListingService {

    private final ListingRepository listingRepository;

    public ListingService(@Qualifier("listingRepository") ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    public ListingEntity createListing(ListingEntity newListing) {
        this.listingRepository.save(newListing);
        return newListing;
    }

    public List<ListingEntity> getListings() {
        return this.listingRepository.findAll();
    }

    public ListingEntity getListingById(UUID id) {
        Optional<ListingEntity> foundListing = this.listingRepository.findById(id);

        if (!foundListing.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "For the provided listing id no listing was found");
        }

        return foundListing.get();
    }

    public List<ListingEntity> getListingByProfileId(UUID profileId) {
        return listingRepository.findByListerId(profileId);
    }

    public void deleteListingById(UUID id) {

        Optional<ListingEntity> foundListing = this.listingRepository.findById(id);

        if (!foundListing.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "For the provided listing id no listing was found");
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
}
