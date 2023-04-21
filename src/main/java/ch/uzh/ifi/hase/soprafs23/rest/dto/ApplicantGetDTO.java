package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import ch.uzh.ifi.hase.soprafs23.constant.ApplicationState;

public class ApplicantGetDTO {
    private UUID applicantId;
    private UUID applicationId;
    private String firstname;
    private String lastname;
    private LocalDateTime applicationDate;
    private ApplicationState state;

    public UUID getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(UUID applicantId) {
        this.applicantId = applicantId;
    }

    public ApplicationState getState() {
        return state;
    }

    public void setState(ApplicationState state) {
        this.state = state;
    }

    public UUID getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(UUID applicationId) {
        this.applicationId = applicationId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }
}
