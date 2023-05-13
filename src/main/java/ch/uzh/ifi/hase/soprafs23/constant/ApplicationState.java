package ch.uzh.ifi.hase.soprafs23.constant;

public enum ApplicationState {
  PENDING(ApplicationState::validatePending),
  DECLINED(ApplicationState::validateDeclined),
  ACCEPTED(ApplicationState::validateAccepted),
  MOVEIN(ApplicationState::validateMoveIn);

  private ITransitionValidator transitionValidator;
  private static final boolean NOTPOSSIBLE = false;

  private ApplicationState(ITransitionValidator transitionValidator) {
    this.transitionValidator = transitionValidator;
  }

  public boolean isTransitionValid(ApplicationState next) {
    return transitionValidator.validate(next, true) ||  transitionValidator.validate(next, false);
  }

  private static boolean validatePending(ApplicationState next, Boolean isLister) {
    return !isLister && (next == ApplicationState.DECLINED || next == ApplicationState.ACCEPTED);
  }

  private static boolean validateDeclined(ApplicationState next, Boolean isLister) {
    return NOTPOSSIBLE;
  }

  private static boolean validateAccepted(ApplicationState next, Boolean isLister) {
    return isLister && (next == ApplicationState.MOVEIN || next == ApplicationState.DECLINED);
  }

  private static boolean validateMoveIn(ApplicationState next, Boolean isLister) {
    return NOTPOSSIBLE;
  }
}
