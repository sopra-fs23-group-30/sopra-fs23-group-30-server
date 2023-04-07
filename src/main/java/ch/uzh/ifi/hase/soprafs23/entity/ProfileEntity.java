package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.ProfileStatus;

import javax.persistence.*;

import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */
@Entity
@Table(name = "PROFILE")
public class ProfileEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  @Type(type = "org.hibernate.type.UUIDCharType")
  private UUID id;

  @Column(nullable = false)
  private String firstname;

  @Column(nullable = false)
  private String lastname;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String phoneNumber;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private boolean isSearcher;

  @Column(nullable = true)
  private Date birthday;

  @Column(nullable = true)
  private String gender;

  @Column(nullable = true)
  private String biography;

  @Column(nullable = true)
  private String futureFlatmatesDescription;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
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