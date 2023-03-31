package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.entity.Profile;
import ch.uzh.ifi.hase.soprafs23.rest.dto.ProfileGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.RegisterProfileDTO;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-31T13:53:10+0200",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 17.0.5 (JetBrains s.r.o.)"
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
        profileGetDTO.setStatus( profile.getStatus() );

        return profileGetDTO;
    }
}
