package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.constant.ProfileStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Profile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DataJpaTest
public class ProfileRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private ProfileRepository profileRepository;

  @Test
  public void findByName_success() {
    // given
    Profile profile = new Profile();
    profile.setName("Firstname Lastname");
    profile.setUsername("firstname@lastname");
    profile.setStatus(ProfileStatus.OFFLINE);
    profile.setToken("1");

    entityManager.persist(profile);
    entityManager.flush();

    // when
    Profile found = profileRepository.findByName(profile.getName());

    // then
    assertNotNull(found.getId());
    assertEquals(found.getName(), profile.getName());
    assertEquals(found.getUsername(), profile.getUsername());
    assertEquals(found.getToken(), profile.getToken());
    assertEquals(found.getStatus(), profile.getStatus());
  }
}
