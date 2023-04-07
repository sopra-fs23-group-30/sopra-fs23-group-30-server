package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.sql.Date;

public class ProfileGetDTO {

  private String firstname;
  private String lastname;
  private Date birthday;
  private String phoneNumber;
  private String gender;
  private String biography;
  private String futureFlatmatesDescription;

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

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getBiography() {
    return biography;
  }

  public void setBiography(String biography) {
    this.biography = biography;
  }

  public String getFutureFlatmatesDescription() {
    return futureFlatmatesDescription;
  }

  public void setFutureFlatmatesDescription(String futureFlatmatesDescription) {
    this.futureFlatmatesDescription = futureFlatmatesDescription;
  }
}
