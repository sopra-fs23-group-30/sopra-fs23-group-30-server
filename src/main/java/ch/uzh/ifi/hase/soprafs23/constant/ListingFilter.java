package ch.uzh.ifi.hase.soprafs23.constant;

import java.util.Arrays;
import java.util.List;

import ch.uzh.ifi.hase.soprafs23.entity.ListingEntity;

public class ListingFilter {
    private float maxRentPerMonth;
    private int flatmateCapacity;
    private SortBy sortBy;

    private List<String> keywords;
    private KMPStringMatcher stringMatcher = new KMPStringMatcher();

    private final int PRIORITIZED_SORT_VALUE_FACTOR = 10000;

    public ListingFilter(String searchText, float maxRentPerMonth, int flatmateCapacity, SortBy sortBy) {
        this.keywords = Arrays.asList(searchText.split(" "));
        this.maxRentPerMonth = maxRentPerMonth;
        this.flatmateCapacity = flatmateCapacity;
        this.sortBy = sortBy;
    }

    public int sortValue(ListingEntity listingEntity) {
        if (!listingIsApplicable(listingEntity)) {
            return Integer.MIN_VALUE;
        }
        int value = 0;
        for (String keyword : keywords) {
            value += stringMatcher.countOccurences(listingEntity.getTitle(), keyword);
            value += stringMatcher.countOccurences(listingEntity.getDescription(), keyword);
            value += stringMatcher
                    .countOccurences(listingEntity.getPerfectFlatmateDescription(), keyword);
            
            value += stringMatcher.countOccurences(listingEntity.getStreetName(), keyword) * PRIORITIZED_SORT_VALUE_FACTOR;
            value += stringMatcher.countOccurences(listingEntity.getCityName(), keyword) * PRIORITIZED_SORT_VALUE_FACTOR;
            value += stringMatcher.countOccurences(((Integer)listingEntity.getZipCode()).toString(), keyword) * PRIORITIZED_SORT_VALUE_FACTOR;
        }
        value += sortBy.getValue(listingEntity);
        return value;
    }

    private boolean listingIsApplicable(ListingEntity listingEntity) {
        return listingEntity.getPricePerMonth() <= maxRentPerMonth;
    }
}
