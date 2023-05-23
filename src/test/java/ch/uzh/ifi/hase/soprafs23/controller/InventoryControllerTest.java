package ch.uzh.ifi.hase.soprafs23.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.uzh.ifi.hase.soprafs23.entity.InventoryItemEntity;
import ch.uzh.ifi.hase.soprafs23.rest.dto.inventory.InventoryItemPostDTO;
import ch.uzh.ifi.hase.soprafs23.service.InventoryItemService;

@WebMvcTest(InventoryController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ContextConfiguration(classes = InventoryController.class)
public class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WebSocketController webSocketController;

    @MockBean
    private InventoryItemService inventoryItemService;

    private InventoryItemEntity inventoryItemEntity;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new InventoryController(webSocketController, inventoryItemService))
                .build();

        inventoryItemEntity = new InventoryItemEntity();
        inventoryItemEntity.setId(UUID.randomUUID());
        inventoryItemEntity.setInventoryId(UUID.randomUUID());
        inventoryItemEntity.setText("Bed");
        inventoryItemEntity.setIsSearcher(true);
        inventoryItemEntity.setIsSelected(false);
    }

    @Test
    void getInventoryItemsById_validInput_success() throws Exception {
        List<InventoryItemEntity> inventory = new ArrayList<>();
        Mockito.when(inventoryItemService.getItemsByInventoryId(Mockito.any())).thenReturn(inventory);

        MockHttpServletRequestBuilder getRequest = get("/inventories/" + inventoryItemEntity.getInventoryId());

        mockMvc.perform(getRequest).andExpect(status().isOk());
    }

    @Test
    void addInventoryItem_validInput_success() throws Exception {
        InventoryItemPostDTO inventoryItemPostDTO = new InventoryItemPostDTO();
        inventoryItemPostDTO.setInventoryId(inventoryItemEntity.getInventoryId());
        inventoryItemPostDTO.setText(inventoryItemEntity.getText());
        inventoryItemPostDTO.setIsSearcher(inventoryItemEntity.getIsSearcher());
        inventoryItemPostDTO.setIsSelected(inventoryItemEntity.getIsSelected());

        Mockito.when(inventoryItemService.createInventoryItem(Mockito.any())).thenReturn(inventoryItemEntity);

        MockHttpServletRequestBuilder postRequest = post("/inventories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(inventoryItemPostDTO));

        mockMvc.perform(postRequest).andExpect(status().isCreated());
    }

    @Test
    void updateInventoryItem_validInput_success() throws Exception {
        InventoryItemPostDTO inventoryItemPostDTO = new InventoryItemPostDTO();
        inventoryItemPostDTO.setInventoryId(inventoryItemEntity.getInventoryId());
        inventoryItemPostDTO.setText("Bunk bed");
        inventoryItemPostDTO.setIsSearcher(inventoryItemEntity.getIsSearcher());
        inventoryItemPostDTO.setIsSelected(!inventoryItemEntity.getIsSelected());

        Mockito.when(inventoryItemService.update(Mockito.any())).thenReturn(inventoryItemEntity);

        MockHttpServletRequestBuilder putRequest = put("/inventories/" + inventoryItemEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(inventoryItemPostDTO));

        mockMvc.perform(putRequest).andExpect(status().isNoContent());
    }

    @Test
    void deleteInventoryItem_validInput_success() throws Exception {
        Mockito.when(inventoryItemService.getByItemId(Mockito.any())).thenReturn(inventoryItemEntity);

        MockHttpServletRequestBuilder deleteRequest = delete("/inventories/" + inventoryItemEntity.getId());

        mockMvc.perform(deleteRequest).andExpect(status().isNoContent());
    }

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }
}
