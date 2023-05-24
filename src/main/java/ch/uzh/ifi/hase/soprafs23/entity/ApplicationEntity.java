package ch.uzh.ifi.hase.soprafs23.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import ch.uzh.ifi.hase.soprafs23.constant.ApplicationState;

@Entity
@Table(name = "APPLICATION")
public class ApplicationEntity implements Serializable {

    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @ManyToOne( fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "listingId", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ListingEntity listing;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "applicantId", referencedColumnName = "id")
    private ProfileEntity applicant;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime creationDate;

    @Enumerated(EnumType.STRING)
    private ApplicationState state;

    @Column(nullable = true)
    private UUID inventoryId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ListingEntity getListing() {
        return listing;
    }

    public void setListing(ListingEntity listing) {
        this.listing = listing;
    }

    public ProfileEntity getApplicant() {
        return applicant;
    }

    public void setApplicant(ProfileEntity applicant) {
        this.applicant = applicant;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ApplicationState getState() {
        return state;
    }

    public void setState(ApplicationState state) {
        this.state = state;
    }

    public UUID getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(UUID inventoryId) {
        this.inventoryId = inventoryId;
    }

}