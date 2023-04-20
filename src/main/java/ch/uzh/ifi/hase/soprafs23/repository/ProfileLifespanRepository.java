package ch.uzh.ifi.hase.soprafs23.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.uzh.ifi.hase.soprafs23.entity.ProfileLifespanEntity;

@Repository("profileLifespanRepository")
public interface ProfileLifespanRepository extends JpaRepository<ProfileLifespanEntity, UUID> {

  List<ProfileLifespanEntity> findByProfileId(UUID id);
}
