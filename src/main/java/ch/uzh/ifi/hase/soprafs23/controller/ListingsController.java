package ch.uzh.ifi.hase.soprafs23.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.uzh.ifi.hase.soprafs23.entity.ListingEntity;
import ch.uzh.ifi.hase.soprafs23.rest.dto.listing.ListingDetailsGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.listing.ListingGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.listing.ListingPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.listing.ListingPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.BlobUploaderService;
import ch.uzh.ifi.hase.soprafs23.service.ListingService;
import ch.uzh.ifi.hase.soprafs23.service.ProfileService;

@RestController
@CrossOrigin
public class ListingsController {

    private final ListingService listingService;
    private ProfileService profileService;
    private BlobUploaderService blobUploaderService;

    ListingsController(ListingService listingService, ProfileService profileService, BlobUploaderService blobUploaderService) {
        this.listingService = listingService;
        this.profileService = profileService;
        this.blobUploaderService = blobUploaderService;
    }

    @PostMapping("/listings")
    public ResponseEntity<Object> createListing(@RequestBody ListingPostDTO listingDTO) {
        ListingEntity listingEntity = DTOMapper.INSTANCE.convertListingPostDTOToListingEntity(listingDTO);
        listingEntity.setLister(profileService.getProfileById(listingDTO.getListerId()));
        ListingEntity createdListing = listingService.createListing(listingEntity);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdListing);
    }

    @GetMapping("/listings")
    public ResponseEntity<List<ListingGetDTO>> getListingById() {
        List<ListingEntity> listingEntities = listingService.getListings();
        List<ListingGetDTO> listingDTOs = new ArrayList<>();
        listingEntities
                .forEach(entity -> listingDTOs.add(DTOMapper.INSTANCE.convertListingEntityToListingGetDTO(entity)));

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

    @PutMapping(value = "/listings/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Object> updateProfileByid(@PathVariable UUID id, 
                                                    @RequestPart("body") String updatedListing,
                                                    @RequestPart("files") MultipartFile[] files) throws IOException {

        ListingPutDTO updateListing = new ListingPutDTO();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            updateListing = objectMapper.readValue(updatedListing, ListingPutDTO.class);
        } catch(IOException e) {

        }

        List<String> blobURLs;
        blobURLs = blobUploaderService.uploadImages(files, id.toString());

        // blobURLs.add(blobUploaderService.upload(file, "listing", id.toString() + fileIndex));
        
        listingService.updateListing(id, updateListing);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }
}
