package ch.uzh.ifi.hase.soprafs23.constant;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ApplicationStateTest {
    @Test
    void isTransitionValid_pendingToDeclined_trueExpected() {
        ApplicationState applicationState = ApplicationState.PENDING;
        assertTrue(applicationState.isTransitionValid(ApplicationState.DECLINED));
    }

    @Test
    void isTransitionValid_pendingToAccepted_trueExpected() {
        ApplicationState applicationState = ApplicationState.PENDING;
        assertTrue(applicationState.isTransitionValid(ApplicationState.ACCEPTED));
    }

    @Test
    void isTransitionValid_pendingToMoveInOrPending_falseExpected() {
        ApplicationState applicationState = ApplicationState.PENDING;
        assertFalse(applicationState.isTransitionValid(ApplicationState.MOVEIN));
        assertFalse(applicationState.isTransitionValid(ApplicationState.PENDING));
    }

    @Test
    void isTransitionValid_acceptedToDeclined_trueExpected() {
        ApplicationState applicationState = ApplicationState.ACCEPTED;
        assertTrue(applicationState.isTransitionValid(ApplicationState.DECLINED));
    }

    @Test
    void isTransitionValid_acceptedToMoveIn_trueExpected() {
        ApplicationState applicationState = ApplicationState.ACCEPTED;
        assertTrue(applicationState.isTransitionValid(ApplicationState.MOVEIN));
    }

    @Test
    void isTransitionValid_acceptedToPendingOrAccepted_falseExpected() {
        ApplicationState applicationState = ApplicationState.ACCEPTED;
        assertFalse(applicationState.isTransitionValid(ApplicationState.PENDING));
        assertFalse(applicationState.isTransitionValid(ApplicationState.ACCEPTED));
    }

    @Test
    void isTransitionValid_declinedToAny_falseExpected() {
        ApplicationState applicationState = ApplicationState.DECLINED;
        assertFalse(applicationState.isTransitionValid(ApplicationState.PENDING));
        assertFalse(applicationState.isTransitionValid(ApplicationState.ACCEPTED));
        assertFalse(applicationState.isTransitionValid(ApplicationState.DECLINED));
        assertFalse(applicationState.isTransitionValid(ApplicationState.MOVEIN));
    }

    @Test
    void isTransitionValid_MoveInToAny_falseExpected() {
        ApplicationState applicationState = ApplicationState.MOVEIN;
        assertFalse(applicationState.isTransitionValid(ApplicationState.PENDING));
        assertFalse(applicationState.isTransitionValid(ApplicationState.ACCEPTED));
        assertFalse(applicationState.isTransitionValid(ApplicationState.DECLINED));
        assertFalse(applicationState.isTransitionValid(ApplicationState.MOVEIN));
    }
}
