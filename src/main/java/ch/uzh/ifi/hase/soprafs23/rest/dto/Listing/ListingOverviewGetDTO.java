package ch.uzh.ifi.hase.soprafs23.rest.dto.listing;

import java.util.List;
import java.util.UUID;

import ch.uzh.ifi.hase.soprafs23.rest.dto.ApplicantGetDTO;

public class ListingOverviewGetDTO {
    private UUID listingId;
    private List<ApplicantGetDTO> applicants;
    private String listingTitle;

    public UUID getListingId() {
        return listingId;
    }
    public void setListingId(UUID listingId) {
        this.listingId = listingId;
    }
    public List<ApplicantGetDTO> getApplicants() {
        return applicants;
    }
    public void setApplicants(List<ApplicantGetDTO> applicants) {
        this.applicants = applicants;
    }
    public String getListingTitle() {
        return listingTitle;
    }
    public void setListingTitle(String listingTitle) {
        this.listingTitle = listingTitle;
    }
}
