package com.ava.foodlogger.service.dto;

import com.ava.foodlogger.domain.enumeration.ActivityLevel;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.ava.foodlogger.domain.AppUser} entity.
 */
public class AppUserDTO implements Serializable {

    private Long id;

    @Lob
    private String bio;

    private LocalDate createdDate;

    @NotNull
    private Float height;

    private Integer workoutsPerWeek;

    private Integer minutesPerWorkout;

    private LocalDate dateOfBirth;

    private ActivityLevel activityLevel;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Integer getWorkoutsPerWeek() {
        return workoutsPerWeek;
    }

    public void setWorkoutsPerWeek(Integer workoutsPerWeek) {
        this.workoutsPerWeek = workoutsPerWeek;
    }

    public Integer getMinutesPerWorkout() {
        return minutesPerWorkout;
    }

    public void setMinutesPerWorkout(Integer minutesPerWorkout) {
        this.minutesPerWorkout = minutesPerWorkout;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public ActivityLevel getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(ActivityLevel activityLevel) {
        this.activityLevel = activityLevel;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUserDTO)) {
            return false;
        }

        AppUserDTO appUserDTO = (AppUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUserDTO{" +
            "id=" + getId() +
            ", bio='" + getBio() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", height=" + getHeight() +
            ", workoutsPerWeek=" + getWorkoutsPerWeek() +
            ", minutesPerWorkout=" + getMinutesPerWorkout() +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", activityLevel='" + getActivityLevel() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
