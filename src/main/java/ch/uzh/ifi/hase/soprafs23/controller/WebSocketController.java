package ch.uzh.ifi.hase.soprafs23.controller;

import java.util.UUID;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import ch.uzh.ifi.hase.soprafs23.entity.ApplicationEntity;
import ch.uzh.ifi.hase.soprafs23.rest.dto.inventory.InventoryItemGetDTO;
import ch.uzh.ifi.hase.soprafs23.service.WebSocketService;

@Controller
public class WebSocketController {

    private static final String INVENTORY_URL = "/inventories/";
    private static final String APPLICATION_URL = "/applications/";

    private final WebSocketService webSocketService;

    public WebSocketController(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @MessageMapping("/inventories/{inventoryId}")
    public void inventoryItemToUsers(UUID inventoryId, InventoryItemGetDTO inventoryItemDTO) {        
        this.webSocketService.sendMessageToClients(INVENTORY_URL + inventoryId, inventoryItemDTO);
    }

    @MessageMapping("/inventories/{inventoryId}/deletedItem")
    public void notifyAboutDelete(UUID inventoryId, UUID inventoryItemId) {        
        this.webSocketService.sendMessageToClients(INVENTORY_URL + inventoryId + "/deletedItem", inventoryItemId);
    }

    @MessageMapping("/applications/{userId}")
    public void applicationStateUpdatedToUser(UUID userId, ApplicationEntity applicationEntity) {        
        this.webSocketService.sendMessageToClients(APPLICATION_URL + userId, applicationEntity);
    }
}