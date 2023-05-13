package ch.uzh.ifi.hase.soprafs23.constant;

import java.util.Arrays;
import java.util.List;

import ch.uzh.ifi.hase.soprafs23.entity.ListingEntity;

public class ListingFilter {
    private float maxRentPerMonth;
    private SortBy sortBy;
    private boolean petsAllowed;
    private boolean elevator;
    private boolean dishwasher;
    private List<String> keywords;
    private KMPStringMatcher stringMatcher = new KMPStringMatcher();

    private final static int prioritizedSortValueFactor = 10000;

    public ListingFilter(String searchText, float maxRentPerMonth, boolean petsAllowed,
            boolean elevator, boolean dishwasher, SortBy sortBy) {
        this.keywords = Arrays.asList(searchText.split(" "));
        this.sortBy = sortBy;
        this.maxRentPerMonth = maxRentPerMonth;
        this.petsAllowed = petsAllowed;
        this.elevator = elevator;
        this.dishwasher = dishwasher;
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

            value += stringMatcher.countOccurences(listingEntity.getAddress(), keyword)
                    * prioritizedSortValueFactor;
        }
        value += sortBy.getValue(listingEntity);
        return value;
    }

    private boolean listingIsApplicable(ListingEntity listingEntity) {
        return listingEntity.getPricePerMonth() <= maxRentPerMonth
                && (listingEntity.getPetsAllowed() || !petsAllowed)
                && (listingEntity.getElevator() || !elevator)
                && (listingEntity.getDishwasher() || !dishwasher);
        // The only case where pets, elevator or dishwasher should return false is when:
        // the lister says false and the searcher says true.
        // In any other case, they either agree, or the searcher doesn't care.
    }
}
