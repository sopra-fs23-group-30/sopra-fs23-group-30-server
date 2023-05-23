package ch.uzh.ifi.hase.soprafs23.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs23.entity.InventoryItemEntity;
import ch.uzh.ifi.hase.soprafs23.repository.InventoryItemRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.inventory.InventoryItemGetDTO;

class InventoryItemServiceTest {

    @Mock
    private InventoryItemRepository inventoryItemRepository;

    @InjectMocks
    private InventoryItemService InventoryItemService;

    private InventoryItemEntity inventoryItemEntity;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        inventoryItemEntity = new InventoryItemEntity();
        inventoryItemEntity.setId(UUID.randomUUID());
        inventoryItemEntity.setInventoryId(UUID.randomUUID());
        inventoryItemEntity.setText("Chair");
        inventoryItemEntity.setIsSearcher(true);
        inventoryItemEntity.setIsSelected(true);
    }

    @Test
    void createInventoryItem_validInput_success() throws Exception {
        Mockito.when(inventoryItemRepository.save(Mockito.any())).thenReturn(inventoryItemEntity);

        InventoryItemEntity foundInventoryItemEntity = InventoryItemService
                .createInventoryItem(inventoryItemEntity);

        assertEquals(inventoryItemEntity.getId(), foundInventoryItemEntity.getId());
        assertEquals(inventoryItemEntity.getInventoryId(), foundInventoryItemEntity.getInventoryId());
        assertEquals(inventoryItemEntity.getText(), foundInventoryItemEntity.getText());
        assertEquals(inventoryItemEntity.getIsSearcher(), foundInventoryItemEntity.getIsSearcher());
        assertEquals(inventoryItemEntity.getIsSelected(), foundInventoryItemEntity.getIsSelected());
    }

    @Test
    void getByItemId_validInput_success() throws Exception {
        Mockito.when(inventoryItemRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(inventoryItemEntity));

        InventoryItemEntity foundInventoryItemEntity = InventoryItemService
                .getByItemId(inventoryItemEntity.getId());

        assertEquals(inventoryItemEntity.getId(), foundInventoryItemEntity.getId());
        assertEquals(inventoryItemEntity.getInventoryId(), foundInventoryItemEntity.getInventoryId());
        assertEquals(inventoryItemEntity.getText(), foundInventoryItemEntity.getText());
        assertEquals(inventoryItemEntity.getIsSearcher(), foundInventoryItemEntity.getIsSearcher());
        assertEquals(inventoryItemEntity.getIsSelected(), foundInventoryItemEntity.getIsSelected());
    }

    @Test
    void getByItemId_invalidInput_responseStatusException() throws Exception {
        Mockito.when(inventoryItemRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        UUID id = inventoryItemEntity.getId();

        assertThrows(ResponseStatusException.class, () -> InventoryItemService
                .getByItemId(id));

    }

    @Test
    void update_validInput_success() throws Exception {
        InventoryItemGetDTO inventoryItemPutDTO = new InventoryItemGetDTO();
        inventoryItemPutDTO.setId(inventoryItemEntity.getId());
        inventoryItemPutDTO.setText("Bucket");
        inventoryItemPutDTO.setIsSelected(inventoryItemEntity.getIsSelected());
        inventoryItemPutDTO.setIsSearcher(inventoryItemEntity.getIsSearcher());
        inventoryItemPutDTO.setInventoryId(inventoryItemEntity.getInventoryId());

        InventoryItemEntity changedInventoryItemEntity = new InventoryItemEntity();
        changedInventoryItemEntity.setId(inventoryItemPutDTO.getId());
        changedInventoryItemEntity.setText(inventoryItemPutDTO.getText());
        changedInventoryItemEntity.setIsSelected(inventoryItemPutDTO.getIsSelected());
        changedInventoryItemEntity.setIsSearcher(inventoryItemPutDTO.getIsSearcher());
        changedInventoryItemEntity.setInventoryId(inventoryItemPutDTO.getInventoryId());

        Mockito.when(inventoryItemRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(inventoryItemEntity));

        Mockito.when(inventoryItemRepository.save(Mockito.any())).thenReturn(changedInventoryItemEntity);

        InventoryItemEntity foundInventoryItemEntity = InventoryItemService
                .update(inventoryItemPutDTO);

        assertEquals(changedInventoryItemEntity.getId(), foundInventoryItemEntity.getId());
        assertEquals(changedInventoryItemEntity.getInventoryId(), foundInventoryItemEntity.getInventoryId());
        assertEquals(changedInventoryItemEntity.getText(), foundInventoryItemEntity.getText());
        assertEquals(changedInventoryItemEntity.getIsSearcher(), foundInventoryItemEntity.getIsSearcher());
        assertEquals(changedInventoryItemEntity.getIsSelected(), foundInventoryItemEntity.getIsSelected());
    }

    @Test
    void getItemsByInventoryId_validInput_success() {
        List<InventoryItemEntity> inventory = new ArrayList<InventoryItemEntity>();
        inventory.add(inventoryItemEntity);

        Mockito.when(inventoryItemRepository.findByInventoryId(Mockito.any())).thenReturn(inventory);

        List<InventoryItemEntity> foundInventory = InventoryItemService
                .getItemsByInventoryId(inventoryItemEntity.getInventoryId());

        assertEquals(1, foundInventory.size());

        InventoryItemEntity foundInventoryItemEntity = foundInventory.get(0);

        assertEquals(inventoryItemEntity.getId(), foundInventoryItemEntity.getId());
        assertEquals(inventoryItemEntity.getInventoryId(), foundInventoryItemEntity.getInventoryId());
        assertEquals(inventoryItemEntity.getText(), foundInventoryItemEntity.getText());
        assertEquals(inventoryItemEntity.getIsSearcher(), foundInventoryItemEntity.getIsSearcher());
        assertEquals(inventoryItemEntity.getIsSelected(), foundInventoryItemEntity.getIsSelected());
    }

    @Test
    void deleteById_validInput_success() {
        assertDoesNotThrow(() -> InventoryItemService.deleteById(inventoryItemEntity.getInventoryId()));
    }
}
