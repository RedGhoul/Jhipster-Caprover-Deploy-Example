package com.ava.foodlogger.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.ava.foodlogger.domain.Food} entity.
 */
public class FoodDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2)
    private String name;

    @NotNull
    private Float calories;

    @NotNull
    private Float carbohydrates;

    @NotNull
    private Float proteins;

    @NotNull
    private Float fat;

    @NotNull
    private Float sodium;

    private Set<FoodEntryDTO> foodEntries = new HashSet<>();

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

    public Float getCalories() {
        return calories;
    }

    public void setCalories(Float calories) {
        this.calories = calories;
    }

    public Float getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(Float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public Float getProteins() {
        return proteins;
    }

    public void setProteins(Float proteins) {
        this.proteins = proteins;
    }

    public Float getFat() {
        return fat;
    }

    public void setFat(Float fat) {
        this.fat = fat;
    }

    public Float getSodium() {
        return sodium;
    }

    public void setSodium(Float sodium) {
        this.sodium = sodium;
    }

    public Set<FoodEntryDTO> getFoodEntries() {
        return foodEntries;
    }

    public void setFoodEntries(Set<FoodEntryDTO> foodEntries) {
        this.foodEntries = foodEntries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FoodDTO)) {
            return false;
        }

        FoodDTO foodDTO = (FoodDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, foodDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FoodDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", calories=" + getCalories() +
            ", carbohydrates=" + getCarbohydrates() +
            ", proteins=" + getProteins() +
            ", fat=" + getFat() +
            ", sodium=" + getSodium() +
            ", foodEntries=" + getFoodEntries() +
            "}";
    }
}
