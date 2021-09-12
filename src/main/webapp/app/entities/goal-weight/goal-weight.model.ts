import * as dayjs from 'dayjs';
import { IAppUser } from 'app/entities/app-user/app-user.model';

export interface IGoalWeight {
  id?: number;
  weight?: number;
  createdDate?: dayjs.Dayjs | null;
  user?: IAppUser | null;
}

export class GoalWeight implements IGoalWeight {
  constructor(public id?: number, public weight?: number, public createdDate?: dayjs.Dayjs | null, public user?: IAppUser | null) {}
}

export function getGoalWeightIdentifier(goalWeight: IGoalWeight): number | undefined {
  return goalWeight.id;
}
