package ch.uzh.ifi.hase.soprafs23.rest.dto.listing;

import java.time.LocalDateTime;
import java.util.UUID;

public class ListingDetailsGetDTO {

    private String title;
    private String description;
    private String streetName;
    private String streetNumber;
    private int zipCode;
    private String cityName;
    private float pricePerMonth;
    private String perfectFlatmateDescription;
    private LocalDateTime creationDate;
    private String listerFirstname;
    private String listerLastname;
    private UUID listerId;
    private String imagesJson;

    public UUID getListerId() {
        return listerId;
    }

    public void setListerId(UUID listerId) {
        this.listerId = listerId;
    }

    public String getListerFirstname() {
        return listerFirstname;
    }

    public void setListerFirstname(String listerFirstname) {
        this.listerFirstname = listerFirstname;
    }

    public String getListerLastname() {
        return listerLastname;
    }

    public void setListerLastname(String listerLastname) {
        this.listerLastname = listerLastname;
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

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
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

    public LocalDateTime getCreation() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getImagesJson() {
        return imagesJson;
    }

    public void setImagesJson(String imagesJson) {
        this.imagesJson = imagesJson;
    }

}
