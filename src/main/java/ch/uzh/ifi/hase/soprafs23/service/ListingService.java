package ch.uzh.ifi.hase.soprafs23.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs23.constant.ApplicationState;
import ch.uzh.ifi.hase.soprafs23.constant.ListingFilter;
import ch.uzh.ifi.hase.soprafs23.entity.ApplicationEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ListingEntity;
import ch.uzh.ifi.hase.soprafs23.repository.ApplicationRepository;
import ch.uzh.ifi.hase.soprafs23.repository.ListingRepository;

@Service
@Primary
@Transactional
public class ListingService {

    private final ListingRepository listingRepository;
    private final ApplicationRepository applicationRepository;

    public ListingService(@Qualifier("listingRepository") ListingRepository listingRepository,
            ApplicationRepository applicationRepository) {
        this.listingRepository = listingRepository;
        this.applicationRepository = applicationRepository;
    }

    public ListingEntity createListing(ListingEntity newListing) {
        this.listingRepository.save(newListing);
        return newListing;
    }

    public List<ListingEntity> getListings(ListingFilter listingFilter) {
        int threshold = (int) (Integer.MIN_VALUE * 0.5);
        List<ListingEntity> listings = this.listingRepository.findAll();
        List<ListingEntity> listsToReturn = new ArrayList<>();
        for (ListingEntity listingEntity : listings) {
            List<ApplicationEntity> applicationsOfListing = applicationRepository
                    .findByListingId(listingEntity.getId());

            if (!hasMovedInState(applicationsOfListing)) {

                listsToReturn.add(listingEntity);
            }
        }

        if(listingFilter == null){
            return listsToReturn;
        }
        
        listsToReturn.removeIf(listing -> (listingFilter.sortValue(listing) < threshold));
        listsToReturn
                .sort((listingA, listingB) -> {
                    int aValue = listingFilter.sortValue(listingA);
                    int bValue = listingFilter.sortValue(listingB);
                    if (aValue < bValue && aValue <= Integer.MIN_VALUE + 1000) {
                        return 1;
                    } else if (bValue <= Integer.MIN_VALUE + 1000) {
                        return -1;
                    }
                    return bValue - aValue;
                });
        return listsToReturn;
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
        UUID listingId = foundListing.get().getId();
        List<ApplicationEntity> applications = applicationRepository
        .findByListingId(listingId);

        for (ApplicationEntity applicationEntity : applications) {
            applicationRepository.deleteById(applicationEntity.getId());
        }

        if (listingRepository.existsById(listingId)) {
            listingRepository.delete(foundListing.get());
        }
    }

    public void updateListing(UUID id, ListingEntity updatedEntity) {
        ListingEntity existingEntity = getListingById(id);
        updatedEntity.setId(existingEntity.getId());
        updatedEntity.setCreationDate(existingEntity.getCreationDate());
        updatedEntity.setLister(existingEntity.getLister());
        this.listingRepository.save(updatedEntity);
    }

    private boolean hasMovedInState(final List<ApplicationEntity> applications) {
        return applications.stream().anyMatch(o -> o.getState().equals(ApplicationState.MOVEIN));
    }
}