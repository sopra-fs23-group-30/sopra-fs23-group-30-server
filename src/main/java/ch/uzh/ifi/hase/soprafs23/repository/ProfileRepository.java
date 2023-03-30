package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface ProfileRepository extends JpaRepository<Profile, Long> {
  Profile findByName(String name);

  Profile findByUsername(String username);
}
