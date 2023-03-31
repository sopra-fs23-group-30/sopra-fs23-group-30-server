package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.constant.ProfileStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Profile;
import ch.uzh.ifi.hase.soprafs23.rest.dto.ProfileGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.RegisterProfileDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation
 * works.
 */
public class DTOMapperTest {
    @Test
    public void testCreateUser_fromUserPostDTO_toUser_success() {
        // create UserPostDTO
        RegisterProfileDTO registerProfileDTO = new RegisterProfileDTO();
        registerProfileDTO.setFirstname("name");
        registerProfileDTO.setLastname("username");

        // MAP -> Create user
        Profile profile = DTOMapper.INSTANCE.convertRegisterProfileDTOtoEntity(registerProfileDTO);

        // check content
        assertEquals(registerProfileDTO.getFirstname(), profile.getFirstname());
        assertEquals(registerProfileDTO.getLastname(), profile.getLastname());
    }

    @Test
    public void testGetUser_fromUser_toUserGetDTO_success() {
        // create User
        Profile profile = new Profile();
        profile.setFirstname("Firstname Lastname");
        profile.setLastname("firstname@lastname");
        profile.setStatus(ProfileStatus.OFFLINE);
        profile.setToken("1");

        // MAP -> Create UserGetDTO
        ProfileGetDTO profileGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(profile);

        // check content
        assertEquals(profile.getId(), profileGetDTO.getId());
        assertEquals(profile.getFirstname(), profileGetDTO.getFirstname());
        assertEquals(profile.getLastname(), profileGetDTO.getLastname());
        assertEquals(profile.getStatus(), profileGetDTO.getStatus());
    }
}
