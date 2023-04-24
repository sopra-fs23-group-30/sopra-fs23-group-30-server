package ch.uzh.ifi.hase.soprafs23.constant;

import java.util.Arrays;
import java.util.List;

import ch.uzh.ifi.hase.soprafs23.entity.ListingEntity;

public class ListingFilter {
    private String searchText;
    private float maxRentPerMonth;
    private int flatmateCapacity;
    private List<String> keywords;

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
        this.keywords = Arrays.asList(searchText.split(" "));
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

    public int sortValue(ListingEntity listingEntity) {
        if (!listingIsApplicable(listingEntity)) {
            return Integer.MIN_VALUE;
        }
        int value = 0;
        for (String keyword : keywords) {
            value += KMPStringMatcher.INSTANCE.countOccurences(listingEntity.getTitle(), keyword);
            value += KMPStringMatcher.INSTANCE.countOccurences(listingEntity.getDescription(), keyword);
            value += KMPStringMatcher.INSTANCE.countOccurences(listingEntity.getPerfectFlatmateDescription(), keyword);
        }

        return value;
    }

    private boolean listingIsApplicable(ListingEntity listingEntity) {
        // TODO: Flatmate capacity
        if (listingEntity.getPricePerMonth() > maxRentPerMonth) {
            return false;
        }
        return true;
    }
}
