package ch.uzh.ifi.hase.soprafs23.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs23.entity.ListingEntity;
import ch.uzh.ifi.hase.soprafs23.rest.dto.Listing.ListingDetailsGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.Listing.ListingGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.Listing.ListingPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.Listing.ListingPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.ListingService;
import ch.uzh.ifi.hase.soprafs23.service.ProfileService;

@RestController
@CrossOrigin
public class ListingsController {

    private final ListingService listingService;
    private ProfileService profileService;

    ListingsController(ListingService listingService, ProfileService profileService) {
        this.listingService = listingService;
        this.profileService = profileService;
    }

    @PostMapping("/listings")
    public ResponseEntity<?> createListing(@RequestBody ListingPostDTO listingDTO) {
        ListingEntity listingEntity = DTOMapper.INSTANCE.convertListingPostDTOToListingEntity(listingDTO);
        listingEntity.setLister(profileService.getProfileById(listingDTO.getListerId()));
        ListingEntity createdListing = listingService.createListing(listingEntity);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdListing);
    }

    @GetMapping("/listings")
    public ResponseEntity<List<ListingGetDTO>> getListingById() {
        // todo: add filtering
        List<ListingEntity> listingEntities = listingService.getListings();
        List<ListingGetDTO> listingDTOs = new ArrayList<ListingGetDTO>();
        listingEntities
                .forEach((entity) -> listingDTOs.add(DTOMapper.INSTANCE.convertListingEntityToListingGetDTO(entity)));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listingDTOs);
    }

    @GetMapping("/listings/{id}")
    public ResponseEntity<ListingDetailsGetDTO> getListingById(@PathVariable UUID id) {
        ListingEntity listingEntity = listingService.getListingById(id);
        ListingDetailsGetDTO listingDTO = DTOMapper.INSTANCE.convertListingEntityToListingDetailsGetDTO(listingEntity);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listingDTO);
    }

    @DeleteMapping("/listings/{id}")
    public ResponseEntity<ListingDetailsGetDTO> deleteListingById(@PathVariable UUID id) {
        listingService.deleteListingById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }

    @PutMapping("/listings/{id}")
    public ResponseEntity<?> updateProfileByid(@PathVariable UUID id, @RequestBody ListingPutDTO updatedListing) {
        listingService.updateListing(id, updatedListing);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }
}
