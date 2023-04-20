package ch.uzh.ifi.hase.soprafs23.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "PROFILELIFESPAN")
public class ProfileLifespanEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  @Type(type = "org.hibernate.type.UUIDCharType")
  private UUID id;

  @ManyToOne(cascade = CascadeType.DETACH)
  @JoinColumn(name = "profileId", referencedColumnName = "id")
  private ProfileEntity profile;

  @Column(nullable = false)
  private Boolean isExperience;

  @Column(nullable = false)
  private String text;

  @Column(nullable = false)
  private Date fromDate;

  @Column(nullable = false)
  private Date toDate;

  public UUID getId() {
    return id;
  }

  public Date getFromDate() {
    return fromDate;
  }

  public void setFromDate(Date fromDate) {
    this.fromDate = fromDate;
  }

  public Date getToDate() {
    return toDate;
  }

  public void setToDate(Date toDate) {
    this.toDate = toDate;
  }

  public ProfileEntity getProfile() {
    return profile;
  }

  public void setProfile(ProfileEntity profile) {
    this.profile = profile;
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

  public Boolean getIsExperience() {
    return isExperience;
  }

}