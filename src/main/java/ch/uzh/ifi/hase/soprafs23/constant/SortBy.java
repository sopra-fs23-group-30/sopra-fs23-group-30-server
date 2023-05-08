package ch.uzh.ifi.hase.soprafs23.constant;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import ch.uzh.ifi.hase.soprafs23.entity.ListingEntity;

public enum SortBy {
    PRICE_ASCENDING(SortBy::priceAscendingValue), 
    PRICE_DESCENDING(SortBy::priceDescendingValue), 
    VIEWS(SortBy::viewsValue), 
    NEWEST(SortBy::newestValue);

    private final ISortByCalculator sortByCalculator;
    private static final int MAX_VALUE = 10000;

    private SortBy(ISortByCalculator sortByCalculator) {
        this.sortByCalculator = sortByCalculator;
    }

    public int getValue(ListingEntity listingEntity) {
        return sortByCalculator.execute(listingEntity);
    }

    private static int priceAscendingValue(ListingEntity listingEntity) {
        return (int)(MAX_VALUE - listingEntity.getPricePerMonth());
    }

    private static int priceDescendingValue(ListingEntity listingEntity) {
        return (int)listingEntity.getPricePerMonth();
    }

    private static int viewsValue(ListingEntity listingEntity) {
        return 0;
    }

    private static int newestValue(ListingEntity listingEntity) {
        return (int)ChronoUnit.DAYS.between(listingEntity.getCreationDate(), LocalDateTime.now());
    }
}
