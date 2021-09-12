import { IFoodDay } from 'app/entities/food-day/food-day.model';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { IFood } from 'app/entities/food/food.model';
import { MealType } from 'app/entities/enumerations/meal-type.model';

export interface IFoodEntry {
  id?: number;
  mealtype?: MealType | null;
  foodDay?: IFoodDay | null;
  user?: IAppUser | null;
  foods?: IFood[] | null;
}

export class FoodEntry implements IFoodEntry {
  constructor(
    public id?: number,
    public mealtype?: MealType | null,
    public foodDay?: IFoodDay | null,
    public user?: IAppUser | null,
    public foods?: IFood[] | null
  ) {}
}

export function getFoodEntryIdentifier(foodEntry: IFoodEntry): number | undefined {
  return foodEntry.id;
}
