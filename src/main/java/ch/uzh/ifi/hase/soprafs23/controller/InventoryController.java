package ch.uzh.ifi.hase.soprafs23.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs23.entity.InventoryItemEntity;
import ch.uzh.ifi.hase.soprafs23.rest.dto.inventory.InventoryItemGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.inventory.InventoryItemPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.InventoryItemService;

@RestController
@CrossOrigin
public class InventoryController {

  private WebSocketController webSocketController;
private InventoryItemService inventoryItemService;

  InventoryController(WebSocketController webSocketController, InventoryItemService inventoryItemService) {
        this.webSocketController = webSocketController;
      this.inventoryItemService = inventoryItemService;}

  
  @GetMapping(value = "/inventories/{inventoryId}")
    public ResponseEntity<List<InventoryItemGetDTO>> getInventoryItemsById(@PathVariable UUID inventoryId){
            List<InventoryItemEntity> entities = inventoryItemService.getItemsByInventoryId(inventoryId);
            List<InventoryItemGetDTO> dtos = new ArrayList<>();
            for (InventoryItemEntity inventoryEntity : entities) {
                dtos.add(DTOMapper.INSTANCE.convertInventoryItemEntityDTOtoInventoryItemGetDto(inventoryEntity));
            }

            return ResponseEntity.status(HttpStatus.OK).body(dtos);
      }    

  @PostMapping(value = "/inventories")
  public ResponseEntity<Object> addInventoryItem(@RequestBody InventoryItemPostDTO inventoryItemDTO){
        InventoryItemEntity inventoryItemEntity = DTOMapper.INSTANCE.convertInventoryItemPostDTOtoInventoryItemEntity(inventoryItemDTO);
        InventoryItemEntity createdItem =inventoryItemService.createInventoryItem(inventoryItemEntity);
        InventoryItemGetDTO inventoryItemGetDTO = DTOMapper.INSTANCE.convertInventoryItemEntityDTOtoInventoryItemGetDto(createdItem);
        webSocketController.inventoryItemToUsers(createdItem.getInventoryId(), inventoryItemGetDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @PutMapping(value = "/inventories/{inventoryId}")
  public ResponseEntity<Object> updateInventoryItem(@PathVariable UUID inventoryId, 
  @RequestBody InventoryItemGetDTO inventoryPutDTO){
        InventoryItemEntity updatedItem = inventoryItemService.update(inventoryPutDTO);
        InventoryItemGetDTO inventoryItemGetDTO = DTOMapper.INSTANCE.convertInventoryItemEntityDTOtoInventoryItemGetDto(updatedItem);
        webSocketController.inventoryItemToUsers(inventoryPutDTO.getInventoryId(), inventoryItemGetDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }

  @DeleteMapping(value = "/inventories/{inventoryItemId}")
  public ResponseEntity<Object> deleteInventoryItem(@PathVariable UUID inventoryItemId){
        UUID inventoryId = inventoryItemService.getByItemId(inventoryItemId).getInventoryId();
        inventoryItemService.deleteById(inventoryItemId);      
        webSocketController.notifyAboutDelete(inventoryId, inventoryItemId);  
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }
}