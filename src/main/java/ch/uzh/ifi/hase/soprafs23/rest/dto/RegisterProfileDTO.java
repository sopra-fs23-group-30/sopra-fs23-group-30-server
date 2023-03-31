package ch.uzh.ifi.hase.soprafs23.rest.dto;

import javax.persistence.Column;

public class RegisterProfileDTO {

  private String firstname;

  private String lastname;

  private String eMail;

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

  public String geteMail() {
    return eMail;
  }

  public void seteMail(String eMail) {
    this.eMail = eMail;
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

  public boolean isSearcher() {
    return isSearcher;
  }

  public void setSearcher(boolean searcher) {
    isSearcher = searcher;
  }
}
