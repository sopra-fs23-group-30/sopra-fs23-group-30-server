package ch.uzh.ifi.hase.soprafs23.rest.dto.application;

import java.time.LocalDateTime;
import java.util.UUID;

import ch.uzh.ifi.hase.soprafs23.constant.ApplicationState;

public class ApplicationGetDTO {
    private UUID applicationId;
    private UUID listingId;
    private LocalDateTime creationDate;
    private ApplicationState state;
    private String listingTitle;
    private String listingStreetName;
    private String listingStreetNumber;
    private int listingZipCode;
    private String listingCityName;

    public UUID getApplicationId() {
        return applicationId;
    }
    public void setApplicationId(UUID applicationId) {
        this.applicationId = applicationId;
    }
    public UUID getListingId() {
        return listingId;
    }
    public void setListingId(UUID listingId) {
        this.listingId = listingId;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
    public ApplicationState getState() {
        return state;
    }
    public void setState(ApplicationState state) {
        this.state = state;
    }
    public String getListingTitle() {
        return listingTitle;
    }
    public void setListingTitle(String listingTitle) {
        this.listingTitle = listingTitle;
    }
    public String getListingStreetName() {
        return listingStreetName;
    }
    public void setListingStreetName(String listinStreetName) {
        this.listingStreetName = listinStreetName;
    }
    public String getListingStreetNumber() {
        return listingStreetNumber;
    }
    public void setListingStreetNumber(String listingStreetNumber) {
        this.listingStreetNumber = listingStreetNumber;
    }
    public int getListingZipCode() {
        return listingZipCode;
    }
    public void setListingZipCode(int listingZipCode) {
        this.listingZipCode = listingZipCode;
    }
    public String getListingCityName() {
        return listingCityName;
    }
    public void setListingCityName(String listingCityName) {
        this.listingCityName = listingCityName;
    }
    
}
