package ch.uzh.ifi.hase.soprafs23.constant;

import ch.uzh.ifi.hase.soprafs23.entity.ListingEntity;

public class ListingFilter {
    private String searchText;
    private float maxPrice;
    private int flatmateCapacity;

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public float getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(float maxPrice) {
        this.maxPrice = maxPrice;
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
        value += KMPStringMatcher.INSTANCE.countOccurences(listingEntity.getTitle(), searchText);
        value += KMPStringMatcher.INSTANCE.countOccurences(listingEntity.getDescription(), searchText);
        value += KMPStringMatcher.INSTANCE.countOccurences(listingEntity.getPerfectFlatmateDescription(), searchText);

        return value;
    }

    private boolean listingIsApplicable(ListingEntity listingEntity) {
        // TODO: Flatmate capacity
        if (listingEntity.getPricePerMonth() > maxPrice) {
            return false;
        }
        return true;
    }
}
