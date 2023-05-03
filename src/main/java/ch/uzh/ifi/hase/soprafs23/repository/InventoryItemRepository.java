package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.InventoryItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

@Repository("inventoryItemRepository")
public interface InventoryItemRepository extends JpaRepository<InventoryItemEntity, UUID> {
  Optional<InventoryItemEntity> findById(UUID id);
  List<InventoryItemEntity> findByInventoryId(UUID id);
}
