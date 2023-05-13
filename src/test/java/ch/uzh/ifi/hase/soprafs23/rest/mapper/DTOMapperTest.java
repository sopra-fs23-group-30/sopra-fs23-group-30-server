package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs23.entity.ProfileEntity;
import ch.uzh.ifi.hase.soprafs23.rest.dto.profile.ProfileGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.profile.RegisterPostDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DTOMapperTest {
    @Test
    void testCreateUser_fromUserPostDTO_toUser_success() {
        // create UserPostDTO
        RegisterPostDTO registerProfileDTO = new RegisterPostDTO();
        registerProfileDTO.setFirstname("name");
        registerProfileDTO.setLastname("username");

        ProfileEntity profile = DTOMapper.INSTANCE.convertRegisterProfileDTOtoEntity(registerProfileDTO);

        assertEquals(registerProfileDTO.getFirstname(), profile.getFirstname());
        assertEquals(registerProfileDTO.getLastname(), profile.getLastname());
    }

    @Test
    void testGetUser_fromUser_toUserGetDTO_success() {
        // create User
        ProfileEntity profile = new ProfileEntity();
        profile.setFirstname("Firstname Lastname");
        profile.setLastname("firstname@lastname");

        // MAP -> Create UserGetDTO
        ProfileGetDTO profileGetDTO = DTOMapper.INSTANCE.convertProfileEntityToProfileGetDTO(profile);

        // check content
        assertEquals(profile.getFirstname(), profileGetDTO.getFirstname());
        assertEquals(profile.getLastname(), profileGetDTO.getLastname());
    }
}