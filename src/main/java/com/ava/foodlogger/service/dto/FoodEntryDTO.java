package com.ava.foodlogger.service.dto;

import com.ava.foodlogger.domain.enumeration.MealType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ava.foodlogger.domain.FoodEntry} entity.
 */
public class FoodEntryDTO implements Serializable {

    private Long id;

    private MealType mealtype;

    private FoodDayDTO foodDay;

    private AppUserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MealType getMealtype() {
        return mealtype;
    }

    public void setMealtype(MealType mealtype) {
        this.mealtype = mealtype;
    }

    public FoodDayDTO getFoodDay() {
        return foodDay;
    }

    public void setFoodDay(FoodDayDTO foodDay) {
        this.foodDay = foodDay;
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
        if (!(o instanceof FoodEntryDTO)) {
            return false;
        }

        FoodEntryDTO foodEntryDTO = (FoodEntryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, foodEntryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FoodEntryDTO{" +
            "id=" + getId() +
            ", mealtype='" + getMealtype() + "'" +
            ", foodDay=" + getFoodDay() +
            ", user=" + getUser() +
            "}";
    }
}
