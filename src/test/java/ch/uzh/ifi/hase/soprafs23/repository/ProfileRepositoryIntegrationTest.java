// package ch.uzh.ifi.hase.soprafs23.repository;

// import ch.uzh.ifi.hase.soprafs23.constant.ProfileStatus;
// import ch.uzh.ifi.hase.soprafs23.entity.Profile;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
// import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;

// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
// @DataJpaTest
// public class ProfileRepositoryIntegrationTest {

//     @Autowired
//     private TestEntityManager entityManager;

//     @Autowired
//     private ProfileRepository profileRepository;

//     @Test
//     public void findByEMail_success() {
//         // given
//         Profile profile = new Profile();
//         profile.setFirstname("Firstname");
//         profile.setLastname("Lastname");
//         profile.setEmail("firstname.lastname@example.ch");
//         profile.setPhoneNumber("0781234567");
//         profile.setSearcher(false);
//         profile.setPassword("OneTwoThreeFour");
//         profile.setStatus(ProfileStatus.OFFLINE);
//         profile.setToken("1");

//         entityManager.persist(profile);
//         entityManager.flush();

//         // when
//         Profile found = profileRepository.findByEmail(profile.getEmail());

//         // then
//         assertNotNull(found.getId());
//         assertEquals(found.getFirstname(), profile.getFirstname());
//         assertEquals(found.getLastname(), profile.getLastname());
//         assertEquals(found.getEmail(), profile.getEmail());
//         assertEquals(found.getPhoneNumber(), profile.getPhoneNumber());
//         assertEquals(found.isSearcher(), profile.isSearcher());
//         assertEquals(found.getPassword(), profile.getPassword());
//         assertEquals(found.getToken(), profile.getToken());
//         assertEquals(found.getStatus(), profile.getStatus());
//     }
// }
