package ch.uzh.ifi.hase.soprafs23.rest.dto.inventory;

import java.util.UUID;

public class InventoryItemGetDTO {

    private UUID id;
    private String text;    
    private Boolean isSearcher;
    private Boolean isSelected;
    private UUID inventoryId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }
    
    public UUID getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(UUID inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Boolean getIsSearcher() {
        return isSearcher;
    }

    public void setIsSearcher(Boolean isSearcher) {
        this.isSearcher = isSearcher;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
