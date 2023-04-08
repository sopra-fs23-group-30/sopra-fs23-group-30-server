package ch.uzh.ifi.hase.soprafs23.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.uzh.ifi.hase.soprafs23.entity.ApplicationEntity;

@Repository("applicationRepository")
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, UUID> {

  Optional<ApplicationEntity> findById(UUID id);

  List<ApplicationEntity> findByApplicantId(UUID id);

  List<ApplicationEntity> findByListingId(UUID id);
}
