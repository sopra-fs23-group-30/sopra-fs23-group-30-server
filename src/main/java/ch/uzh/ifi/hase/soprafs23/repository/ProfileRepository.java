package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

import java.util.Optional;

@Repository("profileRepository")
public interface ProfileRepository extends JpaRepository<ProfileEntity, UUID> {
  ProfileEntity findByEmail(String email);

  Optional<ProfileEntity> findById(UUID id);
}
