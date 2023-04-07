package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.entity.ListingEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ProfileEntity;
import ch.uzh.ifi.hase.soprafs23.rest.dto.ListingPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.ProfileGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.ProfilePutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.RegisterPostDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

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
  ProfileEntity convertRegisterProfileDTOtoEntity(RegisterPostDTO registerProfileDTO);

  @Mapping(source = "email", target = "email")
  @Mapping(source = "firstname", target = "firstname")
  @Mapping(source = "lastname", target = "lastname")
  @Mapping(source = "birthday", target = "birthday")
  @Mapping(source = "phoneNumber", target = "phoneNumber")
  @Mapping(source = "gender", target = "gender")
  @Mapping(source = "biography", target = "biography")
  @Mapping(source = "futureFlatmatesDescription", target = "futureFlatmatesDescription")
  ProfileGetDTO convertProfileEntityToProfileGetDTO(ProfileEntity profile);

  @Mapping(source = "firstname", target = "firstname")
  @Mapping(source = "lastname", target = "lastname")
  @Mapping(source = "birthday", target = "birthday")
  @Mapping(source = "phoneNumber", target = "phoneNumber")
  @Mapping(source = "gender", target = "gender")
  @Mapping(source = "biography", target = "biography")
  @Mapping(source = "futureFlatmatesDescription", target = "futureFlatmatesDescription")
  ProfileEntity convertProfilePutDTOToProfileEntity(ProfilePutDTO profile);

  @Mapping(source = "title", target = "title")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "streetName", target = "streetName")
  @Mapping(source = "streetNumber", target = "streetNumber")
  @Mapping(source = "zipCode", target = "zipCode")
  @Mapping(source = "cityName", target = "cityName")
  @Mapping(source = "pricePerMonth", target = "pricePerMonth")
  @Mapping(source = "perfectFlatmateDescription", target = "perfectFlatmateDescription")
  ListingEntity convertListingPostDTOToListingEntity(ListingPostDTO listing);
}