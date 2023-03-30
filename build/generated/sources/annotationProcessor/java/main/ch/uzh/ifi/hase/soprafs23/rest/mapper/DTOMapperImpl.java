package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.entity.Profile;
import ch.uzh.ifi.hase.soprafs23.rest.dto.ProfileGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.ProfilePostDTO;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-30T16:11:54+0200",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 17.0.5 (Amazon.com Inc.)"
)
public class DTOMapperImpl implements DTOMapper {

    @Override
    public Profile convertUserPostDTOtoEntity(ProfilePostDTO profilePostDTO) {
        if ( profilePostDTO == null ) {
            return null;
        }

        Profile profile = new Profile();

        profile.setName( profilePostDTO.getName() );
        profile.setUsername( profilePostDTO.getUsername() );

        return profile;
    }

    @Override
    public ProfileGetDTO convertEntityToUserGetDTO(Profile profile) {
        if ( profile == null ) {
            return null;
        }

        ProfileGetDTO profileGetDTO = new ProfileGetDTO();

        profileGetDTO.setName( profile.getName() );
        profileGetDTO.setId( profile.getId() );
        profileGetDTO.setUsername( profile.getUsername() );
        profileGetDTO.setStatus( profile.getStatus() );

        return profileGetDTO;
    }
}
