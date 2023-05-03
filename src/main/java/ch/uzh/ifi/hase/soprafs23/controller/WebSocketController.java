package ch.uzh.ifi.hase.soprafs23.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import ch.uzh.ifi.hase.soprafs23.rest.dto.inventory.InventoryItemGetDTO;
import ch.uzh.ifi.hase.soprafs23.service.WebSocketService;

@Controller
@CrossOrigin
public class WebSocketController {

    private final Logger log = LoggerFactory.getLogger(WebSocketController.class);
    private static final String INVENTORY_URL = "/inventories/";
    
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
}