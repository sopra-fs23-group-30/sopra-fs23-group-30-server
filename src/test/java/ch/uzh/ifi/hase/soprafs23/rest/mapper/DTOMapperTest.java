package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.constant.ProfileStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Profile;
import ch.uzh.ifi.hase.soprafs23.rest.dto.ProfileGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.ProfilePostDTO;
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
    ProfilePostDTO profilePostDTO = new ProfilePostDTO();
    profilePostDTO.setName("name");
    profilePostDTO.setUsername("username");

    // MAP -> Create user
    Profile profile = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(profilePostDTO);

    // check content
    assertEquals(profilePostDTO.getName(), profile.getName());
    assertEquals(profilePostDTO.getUsername(), profile.getUsername());
  }

  @Test
  public void testGetUser_fromUser_toUserGetDTO_success() {
    // create User
    Profile profile = new Profile();
    profile.setName("Firstname Lastname");
    profile.setUsername("firstname@lastname");
    profile.setStatus(ProfileStatus.OFFLINE);
    profile.setToken("1");

    // MAP -> Create UserGetDTO
    ProfileGetDTO profileGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(profile);

    // check content
    assertEquals(profile.getId(), profileGetDTO.getId());
    assertEquals(profile.getName(), profileGetDTO.getName());
    assertEquals(profile.getUsername(), profileGetDTO.getUsername());
    assertEquals(profile.getStatus(), profileGetDTO.getStatus());
  }
}
