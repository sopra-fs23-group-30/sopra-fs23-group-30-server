package ch.uzh.ifi.hase.soprafs23.rest.dto.profile;

public class ProfileGetDTO extends ProfilePutDTO {

  private String email;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
