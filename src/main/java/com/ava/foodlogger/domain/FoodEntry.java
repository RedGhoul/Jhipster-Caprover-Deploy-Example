package com.ava.foodlogger.domain;

import com.ava.foodlogger.domain.enumeration.MealType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FoodEntry.
 */
@Entity
@Table(name = "food_entry")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FoodEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "mealtype")
    private MealType mealtype;

    @ManyToOne
    @JsonIgnoreProperties(value = { "foodEntries", "user" }, allowSetters = true)
    private FoodDay foodDay;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "foodEntries", "foodDays", "currentWeights", "goalWeights" }, allowSetters = true)
    private AppUser user;

    @ManyToMany(mappedBy = "foodEntries")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "foodEntries" }, allowSetters = true)
    private Set<Food> foods = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FoodEntry id(Long id) {
        this.id = id;
        return this;
    }

    public MealType getMealtype() {
        return this.mealtype;
    }

    public FoodEntry mealtype(MealType mealtype) {
        this.mealtype = mealtype;
        return this;
    }

    public void setMealtype(MealType mealtype) {
        this.mealtype = mealtype;
    }

    public FoodDay getFoodDay() {
        return this.foodDay;
    }

    public FoodEntry foodDay(FoodDay foodDay) {
        this.setFoodDay(foodDay);
        return this;
    }

    public void setFoodDay(FoodDay foodDay) {
        this.foodDay = foodDay;
    }

    public AppUser getUser() {
        return this.user;
    }

    public FoodEntry user(AppUser appUser) {
        this.setUser(appUser);
        return this;
    }

    public void setUser(AppUser appUser) {
        this.user = appUser;
    }

    public Set<Food> getFoods() {
        return this.foods;
    }

    public FoodEntry foods(Set<Food> foods) {
        this.setFoods(foods);
        return this;
    }

    public FoodEntry addFood(Food food) {
        this.foods.add(food);
        food.getFoodEntries().add(this);
        return this;
    }

    public FoodEntry removeFood(Food food) {
        this.foods.remove(food);
        food.getFoodEntries().remove(this);
        return this;
    }

    public void setFoods(Set<Food> foods) {
        if (this.foods != null) {
            this.foods.forEach(i -> i.removeFoodEntry(this));
        }
        if (foods != null) {
            foods.forEach(i -> i.addFoodEntry(this));
        }
        this.foods = foods;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FoodEntry)) {
            return false;
        }
        return id != null && id.equals(((FoodEntry) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FoodEntry{" +
            "id=" + getId() +
            ", mealtype='" + getMealtype() + "'" +
            "}";
    }
}
