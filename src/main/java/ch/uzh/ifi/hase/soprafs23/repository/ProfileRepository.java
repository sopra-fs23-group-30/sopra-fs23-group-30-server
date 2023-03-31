package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("profileRepository")
public interface ProfileRepository extends JpaRepository<Profile, Long> {
  Profile findByeMail(String eMail);

  Optional<Profile> findById(Long id);
}
