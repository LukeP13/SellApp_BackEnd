package org.udg.pds.springtodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.swing.text.View;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "rates")
public class Rate implements Serializable {
    private static final long serialVersionUID = 1L;

    public Rate(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String description;

    @NotNull
    private Date date;

    @NotNull
    private Float rate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rated_user")
    private User ratedUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rating_user")
    private User ratingUser;

    public Rate(String description, Date date, Float rate) {
        this.description = description;
        this.date = date; this.rate = rate;
    }

    @JsonView(Views.Private.class)
    public Long getId() {
        return id;
    }

     @JsonView(Views.Private.class)
    public String getDescription() {
        return description;
    }

    @JsonView(Views.Private.class)
    public Date getDate() {
        return date;
    }

    @JsonView(Views.Private.class)
    public Float getRate() {
        return rate;
    }

    @JsonView(Views.Private.class)
    public String getRatingUserName() {
        return ratingUser.getName();
    }

    @JsonView(Views.Private.class)
    public Image getUserImage() {
        return ratingUser.getProfileImage();
    }

    @JsonIgnore
    public User getRatedUser() {
        return ratedUser;
    }

    @JsonIgnore
    public User getRatingUser() {
        return ratingUser;
    }

    public void setRatedUser(User ratedUser) {
        this.ratedUser = ratedUser;
    }

    public void setRatingUser(User ratingUser) {
        this.ratingUser = ratingUser;
    }
}
