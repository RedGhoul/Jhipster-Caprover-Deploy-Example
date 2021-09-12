package com.ava.foodlogger.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.ava.foodlogger.domain.FoodDay} entity.
 */
public class FoodDayDTO implements Serializable {

    private Long id;

    private LocalDate createdDate;

    private AppUserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public AppUserDTO getUser() {
        return user;
    }

    public void setUser(AppUserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FoodDayDTO)) {
            return false;
        }

        FoodDayDTO foodDayDTO = (FoodDayDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, foodDayDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FoodDayDTO{" +
            "id=" + getId() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
