package ch.uzh.ifi.hase.soprafs23.rest.dto.application;

import java.util.UUID;

public class ApplicationPostDTO {
    private UUID listingId;

    public UUID getListingId() {
        return listingId;
    }

    private UUID applicantId;

    public UUID getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(UUID applicantId) {
        this.applicantId = applicantId;
    }
}
