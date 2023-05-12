package ch.uzh.ifi.hase.soprafs23.rest.dto.listing;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;

public class ListingDetailsGetDTO {

    private String title;
    private String description;
    private String address;
    private float lattitude;
    private float longitude;
    private float pricePerMonth;
    private String perfectFlatmateDescription;
    private LocalDateTime creationDate;
    private boolean petsAllowed;
    private boolean elevator;
    private boolean dishwasher;
    private String listerFirstname;
    private String listerLastname;
    private String listerDescription;
    private Date listerBirthdate;
    private String profilePictureURL;

    private UUID listerId;
    private String imagesJson;

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

    public UUID getListerId() {
        return listerId;
    }

    public void setListerId(UUID listerId) {
        this.listerId = listerId;
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

    public String getListerDescription() {
        return listerDescription;
    }

    public void setListerDescription(String listerDescription) {
        this.listerDescription = listerDescription;
    }

    public Date getListerBirthdate() {
        return listerBirthdate;
    }

    public void setListerBirthdate(Date listerBirthdate) {
        this.listerBirthdate = listerBirthdate;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public void setProfilePictureURL(String profilePictureURL) {
        this.profilePictureURL = profilePictureURL;
    }

    public String getImagesJson() {
        return imagesJson;
    }

    public void setImagesJson(String imagesJson) {
        this.imagesJson = imagesJson;
    }
}
