import * as dayjs from 'dayjs';
import { IAppUser } from 'app/entities/app-user/app-user.model';

export interface ICurrentWeight {
  id?: number;
  weight?: number;
  createdDate?: dayjs.Dayjs | null;
  user?: IAppUser | null;
}

export class CurrentWeight implements ICurrentWeight {
  constructor(public id?: number, public weight?: number, public createdDate?: dayjs.Dayjs | null, public user?: IAppUser | null) {}
}

export function getCurrentWeightIdentifier(currentWeight: ICurrentWeight): number | undefined {
  return currentWeight.id;
}
