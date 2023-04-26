package ch.uzh.ifi.hase.soprafs23.rest.dto.application;

import ch.uzh.ifi.hase.soprafs23.constant.ApplicationState;

public class ApplicationPutDTO {
    private ApplicationState newState;

    public ApplicationState getNewState() {
        return newState;
    }

    public void setNewState(ApplicationState newState) {
        this.newState = newState;
    }
}
