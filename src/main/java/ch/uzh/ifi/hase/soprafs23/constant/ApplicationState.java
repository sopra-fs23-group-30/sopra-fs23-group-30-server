package ch.uzh.ifi.hase.soprafs23.constant;

public enum ApplicationState {
  PENDING(ApplicationState::validatePending), 
  DECLINED(ApplicationState::validateDeclined), 
  ACCEPTED(ApplicationState::validateAccepted), 
  MOVEIN(ApplicationState::validateMoveIn);

  private ITransitionValidator transitionValidator;

  private ApplicationState(ITransitionValidator transitionValidator) {
    this.transitionValidator = transitionValidator;
  }

  public boolean isTransitionValid(ApplicationState next) {
    return transitionValidator.validate(next);
  } 

  private static boolean validatePending(ApplicationState next) {
    return next == ApplicationState.DECLINED || next == ApplicationState.ACCEPTED;
  }

  private static boolean validateDeclined(ApplicationState next) {
    return false;
  }

  private static boolean validateAccepted(ApplicationState next) {
    return next == ApplicationState.MOVEIN || next == ApplicationState.DECLINED;
  }

  private static boolean validateMoveIn(ApplicationState next) {
    return false;
  }
}
