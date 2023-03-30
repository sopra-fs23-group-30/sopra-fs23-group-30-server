package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.ProfileStatus;

public class ProfileGetDTO {

  private Long id;
  private String name;
  private String username;
  private ProfileStatus status;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public ProfileStatus getStatus() {
    return status;
  }

  public void setStatus(ProfileStatus status) {
    this.status = status;
  }
}
