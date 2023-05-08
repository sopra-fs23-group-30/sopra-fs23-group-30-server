package ch.uzh.ifi.hase.soprafs23.constant;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs23.entity.ListingEntity;

class ListingFilterTest {
        @Test
        void sortValue_validInput_4expected() {
                ListingFilter listingFilter = new ListingFilter("Hello world", 2000, 3, true, true, true,
                                SortBy.PRICE_ASCENDING);

                ListingEntity listingEntity = new ListingEntity();
                listingEntity.setPricePerMonth(1500);
                listingEntity.setTitle("World");
                listingEntity.setDescription("Hello");
                listingEntity.setPerfectFlatmateDescription("World, I say: hello!");
                listingEntity.setAddress("Friedhofsplatz 8, 4500 Solothurn");
                listingEntity.setFlatmateCapacity(2);
                listingEntity.setPetsAllowed(true);
                listingEntity.setElevator(true);
                listingEntity.setDishwasher(true);

                // Expects words + maxValue - rent price
                assertEquals(4 + 10000 - listingEntity.getPricePerMonth(), listingFilter.sortValue(listingEntity));
        }

        @Test
        void sortValue_validInput_minValueExpected() {
                ListingFilter listingFilter = new ListingFilter("Hello world", 2000, 3, true, true, true,
                                SortBy.PRICE_ASCENDING);

                ListingEntity listingEntity = new ListingEntity();
                listingEntity.setPricePerMonth(2500);
                listingEntity.setTitle("World");
                listingEntity.setDescription("Hello");
                listingEntity.setPerfectFlatmateDescription("World, I say: hello!");

                assertEquals(Integer.MIN_VALUE, listingFilter.sortValue(listingEntity));
        }
}
