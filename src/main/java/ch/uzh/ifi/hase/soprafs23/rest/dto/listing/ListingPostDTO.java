package ch.uzh.ifi.hase.soprafs23.rest.dto.listing;

import java.util.UUID;

public class ListingPostDTO {

    private String title;
    private String description;
    private String address;
    private float lattitude;
    private float longitude;
    private float pricePerMonth;
    private String perfectFlatmateDescription;
    private UUID listerId;
    private String imagesJson;
    private int flatmateCapacity;
    private boolean petsAllowed;
    private boolean elevator;
    private boolean dishwasher;
    
    public int getFlatmateCapacity() {
        return flatmateCapacity;
    }

    public void setFlatmateCapacity(int flatmateCapacity) {
        this.flatmateCapacity = flatmateCapacity;
    }

    public boolean getPetsAllowed() {
        return petsAllowed;
    }

    public void setPetsAllowed(boolean petsAllowed) {
        this.petsAllowed = petsAllowed;
    }

    public boolean getElevator() {
        return elevator;
    }

    public void setElevator(boolean elevator) {
        this.elevator = elevator;
    }

    public boolean getDishwasher() {
        return dishwasher;
    }

    public void setDishwasher(boolean dishwasher) {
        this.dishwasher = dishwasher;
    }

    public String getAddress() {
        return address;
    }    

    public void setAddress(String address) {
        this.address = address;
    }    

    public float getLattitude() {
        return lattitude;
    }    

    public void setLattitude(float lattitude) {
        this.lattitude = lattitude;
    }    

    public float getLongitude() {
        return longitude;
    }    

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }    

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPricePerMonth() {
        return pricePerMonth;
    }

    public void setPricePerMonth(float pricePerMonth) {
        this.pricePerMonth = pricePerMonth;
    }

    public String getPerfectFlatmateDescription() {
        return perfectFlatmateDescription;
    }

    public void setPerfectFlatmateDescription(String perfectFlatmateDescription) {
        this.perfectFlatmateDescription = perfectFlatmateDescription;
    }

    public UUID getListerId() {
        return listerId;
    }

    public void setListerId(UUID listerId) {
        this.listerId = listerId;
    }

    public String getImagesJson() {
        return imagesJson;
    }

    public void setImagesJson(String imagesJson) {
        this.imagesJson = imagesJson;
    }
}
