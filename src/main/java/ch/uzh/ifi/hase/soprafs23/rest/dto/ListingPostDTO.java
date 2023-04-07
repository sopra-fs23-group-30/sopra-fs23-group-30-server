package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class ListingPostDTO {

    private String title;
    private String description;
    private String streetName;
    private String streetNumber;
    private int zipCode;
    private String cityName;
    private float pricePerMonth;
    private String perfectFlatmateDescription;

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
}
