package ch.uzh.ifi.hase.soprafs23.rest.dto.Profile;

public class RegisterPostDTO {

  private String firstname;

  private String lastname;

  private String email;

  private String phoneNumber;

  private String password;

  private boolean isSearcher;

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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean getIsSearcher() {
    return isSearcher;
  }

  public void setIsSearcher(boolean searcher) {
    isSearcher = searcher;
  }
}
