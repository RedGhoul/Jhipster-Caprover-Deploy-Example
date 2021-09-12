package com.ava.foodlogger.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.ava.foodlogger.domain.CurrentWeight} entity.
 */
public class CurrentWeightDTO implements Serializable {

    private Long id;

    @NotNull
    private Float weight;

    private LocalDate createdDate;

    private AppUserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
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
        if (!(o instanceof CurrentWeightDTO)) {
            return false;
        }

        CurrentWeightDTO currentWeightDTO = (CurrentWeightDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, currentWeightDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CurrentWeightDTO{" +
            "id=" + getId() +
            ", weight=" + getWeight() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
