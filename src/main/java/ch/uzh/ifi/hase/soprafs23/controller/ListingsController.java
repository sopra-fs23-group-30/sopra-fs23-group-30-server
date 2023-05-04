package ch.uzh.ifi.hase.soprafs23.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.uzh.ifi.hase.soprafs23.constant.ListingFilter;
import ch.uzh.ifi.hase.soprafs23.constant.SortBy;
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

    ListingsController(ListingService listingService, ProfileService profileService,
            BlobUploaderService blobUploaderService) {
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
    public ResponseEntity<List<ListingGetDTO>> getListing(
            @RequestParam String searchText,
            @RequestParam Float maxRentPerMonth,
            @RequestParam int flatmateCapacity, 
            @RequestParam SortBy sortBy) {
        ListingFilter listingFilter = new ListingFilter(searchText, maxRentPerMonth, flatmateCapacity, sortBy);

        List<ListingEntity> listingEntities = listingService.getListings(listingFilter);
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

    @PutMapping(value = "/listings/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Object> updateProfileByid(@PathVariable UUID id,
            @RequestPart("body") String updatedListing,
            @RequestPart("files") MultipartFile[] files) throws IOException {

        ListingPutDTO updateListing = new ListingPutDTO();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            updateListing = objectMapper.readValue(updatedListing, ListingPutDTO.class);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        ListingEntity entityInDb = listingService.getListingById(id);
        List<String> existingImagesInDb = getListOfStrings(entityInDb.getImagesJson());
        List<String> updatedImages = getListOfStrings(updateListing.getImagesJson());
        List<String> toDeleteImages = new ArrayList<>(existingImagesInDb);
        toDeleteImages.removeAll(updatedImages);

        try {
            List<String> blobURLs = blobUploaderService.uploadImages(files, id.toString(), toDeleteImages);
            blobURLs.addAll(updatedImages);
            String jsonString = getJsonString(blobURLs);
            updateListing.setImagesJson(jsonString);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        ListingEntity listingEntity = DTOMapper.INSTANCE.convertListingPostDTOToListingEntity(updateListing);

        listingService.updateListing(id, listingEntity);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }

    private String getJsonString(List<String> blobURLs) throws JSONException {
        JSONArray jsonArray = new JSONArray();

        for (String blobURL : blobURLs) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("imageURL", blobURL);
            jsonArray.put(jsonObject);
        }

        return jsonArray.toString();
    }

    private List<String> getListOfStrings(String jsonString) throws com.fasterxml.jackson.core.JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> imageURLs = new ArrayList<>();

        JsonNode jsonNode = objectMapper.readTree(jsonString);
        Iterator<JsonNode> iterator = jsonNode.iterator();
        while (iterator.hasNext()) {
            JsonNode node = iterator.next();
            String imageURL = node.get("imageURL").asText();
            imageURLs.add(imageURL);
        }
        return imageURLs;
    }
}
