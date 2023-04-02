package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.entity.Profile;
import ch.uzh.ifi.hase.soprafs23.rest.dto.ProfileGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.RegisterProfileDTO;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-04-02T15:36:59+0200",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 17.0.6 (Eclipse Adoptium)"
)
public class DTOMapperImpl implements DTOMapper {

    @Override
    public Profile convertRegisterProfileDTOtoEntity(RegisterProfileDTO registerProfileDTO) {
        if ( registerProfileDTO == null ) {
            return null;
        }

        Profile profile = new Profile();

        profile.setFirstname( registerProfileDTO.getFirstname() );
        profile.setPassword( registerProfileDTO.getPassword() );
        profile.setSearcher( registerProfileDTO.isSearcher() );
        profile.setPhoneNumber( registerProfileDTO.getPhoneNumber() );
        profile.setEmail( registerProfileDTO.getEmail() );
        profile.setLastname( registerProfileDTO.getLastname() );

        return profile;
    }

    @Override
    public ProfileGetDTO convertEntityToUserGetDTO(Profile profile) {
        if ( profile == null ) {
            return null;
        }

        ProfileGetDTO profileGetDTO = new ProfileGetDTO();

        profileGetDTO.setFirstname( profile.getFirstname() );
        profileGetDTO.setId( profile.getId() );
        profileGetDTO.setLastname( profile.getLastname() );

        return profileGetDTO;
    }
}
