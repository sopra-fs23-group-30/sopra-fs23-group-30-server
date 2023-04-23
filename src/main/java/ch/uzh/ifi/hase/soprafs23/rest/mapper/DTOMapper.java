package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import ch.uzh.ifi.hase.soprafs23.constant.ListingFilter;
import ch.uzh.ifi.hase.soprafs23.entity.ApplicationEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ListingEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ProfileEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ProfileLifespanEntity;
import ch.uzh.ifi.hase.soprafs23.rest.dto.ApplicantGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.application.ApplicationGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.application.ApplicationPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.listing.ListingDetailsGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.listing.ListingFilterGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.listing.ListingGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.listing.ListingOverviewGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.listing.ListingPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.profile.ProfileGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.profile.ProfileLifespanDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.profile.ProfilePutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.profile.RegisterPostDTO;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 */
@Mapper
public interface DTOMapper {

  DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

  @Mapping(source = "firstname", target = "firstname")
  @Mapping(source = "lastname", target = "lastname")
  @Mapping(source = "email", target = "email")
  @Mapping(source = "phoneNumber", target = "phoneNumber")
  @Mapping(source = "password", target = "password")
  @Mapping(source = "isSearcher", target = "isSearcher")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "biography", ignore = true)
  @Mapping(target = "birthday", ignore = true)
  @Mapping(target = "futureFlatmatesDescription", ignore = true)
  @Mapping(target = "gender", ignore = true)
  @Mapping(target = "profilePictureURL", ignore = true)
  ProfileEntity convertRegisterProfileDTOtoEntity(RegisterPostDTO registerProfileDTO);

  @Mapping(source = "email", target = "email")
  @Mapping(source = "firstname", target = "firstname")
  @Mapping(source = "lastname", target = "lastname")
  @Mapping(source = "birthday", target = "birthday")
  @Mapping(source = "phoneNumber", target = "phoneNumber")
  @Mapping(source = "gender", target = "gender")
  @Mapping(source = "biography", target = "biography")
  @Mapping(source = "futureFlatmatesDescription", target = "futureFlatmatesDescription")
  @Mapping(target = "lifespans", ignore = true)
  ProfileGetDTO convertProfileEntityToProfileGetDTO(ProfileEntity profile);

  @Mapping(source = "firstname", target = "firstname")
  @Mapping(source = "lastname", target = "lastname")
  @Mapping(source = "birthday", target = "birthday")
  @Mapping(source = "phoneNumber", target = "phoneNumber")
  @Mapping(source = "gender", target = "gender")
  @Mapping(source = "biography", target = "biography")
  @Mapping(source = "futureFlatmatesDescription", target = "futureFlatmatesDescription")
  @Mapping(target = "profilePictureURL", source = "profilePictureURL")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "email", ignore = true)
  @Mapping(target = "isSearcher", ignore = true)
  @Mapping(target = "password", ignore = true)
  ProfileEntity convertProfilePutDTOToProfileEntity(ProfilePutDTO profile);

  @Mapping(source = "title", target = "title")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "streetName", target = "streetName")
  @Mapping(source = "streetNumber", target = "streetNumber")
  @Mapping(source = "zipCode", target = "zipCode")
  @Mapping(source = "cityName", target = "cityName")
  @Mapping(source = "pricePerMonth", target = "pricePerMonth")
  @Mapping(source = "perfectFlatmateDescription", target = "perfectFlatmateDescription")
  @Mapping(source = "imagesJson", target = "imagesJson")
  @Mapping(target = "creationDate", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "lister", ignore = true)
  ListingEntity convertListingPostDTOToListingEntity(ListingPostDTO listing);

  @Mapping(source = "title", target = "title")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "streetName", target = "streetName")
  @Mapping(source = "streetNumber", target = "streetNumber")
  @Mapping(source = "zipCode", target = "zipCode")
  @Mapping(source = "cityName", target = "cityName")
  @Mapping(source = "pricePerMonth", target = "pricePerMonth")
  @Mapping(source = "perfectFlatmateDescription", target = "perfectFlatmateDescription")
  @Mapping(source = "lister.firstname", target = "listerFirstname")
  @Mapping(source = "lister.lastname", target = "listerLastname")
  @Mapping(source = "lister.biography", target = "listerDescription")
  @Mapping(source = "lister.birthday", target = "listerBirthdate")
  @Mapping(source = "lister.id", target = "listerId")
  @Mapping(source = "creationDate", target = "creationDate")
  @Mapping(source = "lister.profilePictureURL", target = "profilePictureURL")
  @Mapping(source = "imagesJson", target = "imagesJson")

  ListingDetailsGetDTO convertListingEntityToListingDetailsGetDTO(ListingEntity listing);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "title", target = "title")
  @Mapping(source = "streetName", target = "streetName")
  @Mapping(source = "streetNumber", target = "streetNumber")
  @Mapping(source = "zipCode", target = "zipCode")
  @Mapping(source = "cityName", target = "cityName")
  @Mapping(source = "pricePerMonth", target = "pricePerMonth")
  @Mapping(source = "imagesJson", target = "imagesJson")
  ListingGetDTO convertListingEntityToListingGetDTO(ListingEntity listing);

  @Mapping(source = "applicantId", target = "applicant.id")
  @Mapping(source = "listingId", target = "listing.id")
  @Mapping(target = "state", ignore = true)
  @Mapping(target = "creationDate", ignore = true)
  @Mapping(target = "id", ignore = true)
  ApplicationEntity convertApplicationPostDTOToApplicationEntity(ApplicationPostDTO applicationDTO);

  @Mapping(source = "id", target = "applicationId")
  @Mapping(source = "listing.id", target = "listingId")
  @Mapping(source = "creationDate", target = "creationDate")
  @Mapping(source = "state", target = "state")
  @Mapping(source = "listing.title", target = "listingTitle")
  @Mapping(source = "listing.streetName", target = "listingStreetName")
  @Mapping(source = "listing.streetNumber", target = "listingStreetNumber")
  @Mapping(source = "listing.zipCode", target = "listingZipCode")
  @Mapping(source = "listing.cityName", target = "listingCityName")
  ApplicationGetDTO convertApplicationEntityToApplicationGetDTO(ApplicationEntity applicationEntity);

  @Mapping(source = "applicant.id", target = "applicantId")
  @Mapping(source = "id", target = "applicationId")
  @Mapping(source = "applicant.firstname", target = "firstname")
  @Mapping(source = "applicant.lastname", target = "lastname")
  @Mapping(source = "creationDate", target = "applicationDate")
  @Mapping(source = "state", target = "state")
  @Mapping(source = "applicant.profilePictureURL", target = "profilePictureURL")
  ApplicantGetDTO convertApplicationEntityToApplicantGetDTO(ApplicationEntity applicationEntity);

  @Mapping(source = "id", target = "listingId")
  @Mapping(target = "applicants", ignore = true)
  @Mapping(source = "title", target = "listingTitle")
  ListingOverviewGetDTO convertListingEntityToListingOverviewGetDTO(ListingEntity listingEntity);

  @Mapping(source = "fromDate", target = "fromDate")
  @Mapping(source = "toDate", target = "toDate")
  @Mapping(source = "text", target = "text")
  ProfileLifespanDTO convertLifespanEntityToLifespanDTO(ProfileLifespanEntity profileLifespanEntity);

  @Mapping(source = "fromDate", target = "fromDate")
  @Mapping(source = "toDate", target = "toDate")
  @Mapping(source = "text", target = "text")
  @Mapping(target = "profile", ignore = true)
  ProfileLifespanEntity convertLifespanDTOToLifespanEntity(ProfileLifespanDTO profileLifespanDTO);

  @Mapping(source = "searchText", target = "searchText")
  @Mapping(source = "maxPrice", target = "maxPrice")
  @Mapping(source = "flatmateCapacity", target = "flatmateCapacity")
  ListingFilter convertListingFiltergetDTOToListingFilter(ListingFilterGetDTO listingFilterGetDTO);
}
