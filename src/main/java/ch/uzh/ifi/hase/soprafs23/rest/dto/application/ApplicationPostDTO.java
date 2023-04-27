package ch.uzh.ifi.hase.soprafs23.rest.dto.application;

import java.util.UUID;

public class ApplicationPostDTO {
    private UUID listingId;
    private UUID applicantId;
    
    public UUID getListingId() {
        return listingId;
    }
    public void setListingId(UUID listingId) {
        this.listingId = listingId;
    }
    public UUID getApplicantId() {
        return applicantId;
    }
    public void setApplicantId(UUID applicantId) {
        this.applicantId = applicantId;
    }
}
