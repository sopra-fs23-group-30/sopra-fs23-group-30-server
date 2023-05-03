package ch.uzh.ifi.hase.soprafs23.rest.dto.listing;

import java.util.UUID;

public class ListingGetDTO {

    private UUID id;
    private String title;
    private String address;
    private float pricePerMonth;
    private String imagesJson;

    public String getAddress() {
        return address;
    }    

    public void setAddress(String address) {
        this.address = address;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public float getPricePerMonth() {
        return pricePerMonth;
    }

    public void setPricePerMonth(float pricePerMonth) {
        this.pricePerMonth = pricePerMonth;
    }

    public String getImagesJson() {
        return imagesJson;
    }

    public void setImagesJson(String imagesJson) {
        this.imagesJson = imagesJson;
    }
}
