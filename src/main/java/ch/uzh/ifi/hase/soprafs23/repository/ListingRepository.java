package ch.uzh.ifi.hase.soprafs23.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.uzh.ifi.hase.soprafs23.entity.ListingEntity;

@Repository("listingRepository")
public interface ListingRepository extends JpaRepository<ListingEntity, UUID> {

  Optional<ListingEntity> findById(UUID id);

  List<ListingEntity> findByListerId(UUID id);
}
