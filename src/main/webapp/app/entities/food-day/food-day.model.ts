import * as dayjs from 'dayjs';
import { IFoodEntry } from 'app/entities/food-entry/food-entry.model';
import { IAppUser } from 'app/entities/app-user/app-user.model';

export interface IFoodDay {
  id?: number;
  createdDate?: dayjs.Dayjs | null;
  foodEntries?: IFoodEntry[] | null;
  user?: IAppUser | null;
}

export class FoodDay implements IFoodDay {
  constructor(
    public id?: number,
    public createdDate?: dayjs.Dayjs | null,
    public foodEntries?: IFoodEntry[] | null,
    public user?: IAppUser | null
  ) {}
}

export function getFoodDayIdentifier(foodDay: IFoodDay): number | undefined {
  return foodDay.id;
}
