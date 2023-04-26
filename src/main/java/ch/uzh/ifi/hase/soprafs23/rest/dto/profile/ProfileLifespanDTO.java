package ch.uzh.ifi.hase.soprafs23.rest.dto.profile;

import java.sql.Date;

public class ProfileLifespanDTO {

  private String text;
  private Date fromDate;
  private Date toDate;
  private Boolean isExperience;

  public Date getToDate() {
    return toDate;
  }

  public void setToDate(Date toDate) {
    this.toDate = toDate;
  }

  public Date getFromDate() {
    return fromDate;
  }

  public void setFromDate(Date fromDate) {
    this.fromDate = fromDate;
  }

  public Boolean getIsExperience() {
    return isExperience;
  }

  public void setIsExperience(Boolean isExperience) {
    this.isExperience = isExperience;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

}
