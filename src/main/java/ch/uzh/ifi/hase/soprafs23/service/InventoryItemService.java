package ch.uzh.ifi.hase.soprafs23.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs23.entity.InventoryItemEntity;
import ch.uzh.ifi.hase.soprafs23.repository.InventoryItemRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.inventory.InventoryItemGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;

@Service
@Primary
@Transactional
public class InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;

    public InventoryItemService(@Qualifier("inventoryItemRepository") InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    public InventoryItemEntity createInventoryItem(InventoryItemEntity newInventory) {
        return this.inventoryItemRepository.save(newInventory);
    }

    public InventoryItemEntity getByItemId(UUID inventoryItemId) {
        Optional<InventoryItemEntity> foundItem = this.inventoryItemRepository.findById(inventoryItemId);

        if (!foundItem.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "For the provided id no inventory item was found");
        }

        return foundItem.get();
    }

    public InventoryItemEntity update(InventoryItemGetDTO inventoryPutDTO) {
        InventoryItemEntity entity = getByItemId(inventoryPutDTO.getId());
        entity.setText(inventoryPutDTO.getText());
        entity.setIsSelected(inventoryPutDTO.getIsSelected());
        return this.inventoryItemRepository.save(entity);
    }

    public List<InventoryItemEntity> getItemsByInventoryId(UUID inventoryId) {
         return  inventoryItemRepository.findByInventoryId(inventoryId);  
    }
}
