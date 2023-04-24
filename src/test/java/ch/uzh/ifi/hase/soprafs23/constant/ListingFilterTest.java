package ch.uzh.ifi.hase.soprafs23.constant;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs23.entity.ListingEntity;

public class ListingFilterTest {
    @Test
    void sortValue_validInput_4expected() {
        ListingFilter listingFilter = new ListingFilter();
        listingFilter.setSearchText("Hello world");
        listingFilter.setFlatmateCapacity(10);
        listingFilter.setMaxPrice(2000);

        ListingEntity listingEntity = new ListingEntity();
        listingEntity.setPricePerMonth(1500);
        listingEntity.setTitle("World");
        listingEntity.setDescription("Hello");
        listingEntity.setPerfectFlatmateDescription("World, I say: hello!");

        assertEquals(4, listingFilter.sortValue(listingEntity));
    }

    @Test
    void sortValue_validInput_minValueExpected() {
        ListingFilter listingFilter = new ListingFilter();
        listingFilter.setSearchText("Hello world");
        listingFilter.setFlatmateCapacity(10);
        listingFilter.setMaxPrice(2000);

        ListingEntity listingEntity = new ListingEntity();
        listingEntity.setPricePerMonth(2500);
        listingEntity.setTitle("World");
        listingEntity.setDescription("Hello");
        listingEntity.setPerfectFlatmateDescription("World, I say: hello!");

        assertEquals(Integer.MIN_VALUE, listingFilter.sortValue(listingEntity));
    }
}
