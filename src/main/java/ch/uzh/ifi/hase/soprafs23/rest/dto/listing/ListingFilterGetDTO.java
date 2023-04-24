package ch.uzh.ifi.hase.soprafs23.rest.dto.listing;

public class ListingFilterGetDTO {
    private String searchText;
    private float maxRentPerMonth;
    private int flatmateCapacity;

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public float getMaxRentPerMonth() {
        return maxRentPerMonth;
    }

    public void setMaxRentPerMonth(float maxPrice) {
        this.maxRentPerMonth = maxPrice;
    }

    public int getFlatmateCapacity() {
        return flatmateCapacity;
    }

    public void setFlatmateCapacity(int flatmateCapacity) {
        this.flatmateCapacity = flatmateCapacity;
    }
}
