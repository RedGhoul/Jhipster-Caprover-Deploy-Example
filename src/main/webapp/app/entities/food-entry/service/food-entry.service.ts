import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFoodEntry, getFoodEntryIdentifier } from '../food-entry.model';

export type EntityResponseType = HttpResponse<IFoodEntry>;
export type EntityArrayResponseType = HttpResponse<IFoodEntry[]>;

@Injectable({ providedIn: 'root' })
export class FoodEntryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/food-entries');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(foodEntry: IFoodEntry): Observable<EntityResponseType> {
    return this.http.post<IFoodEntry>(this.resourceUrl, foodEntry, { observe: 'response' });
  }

  update(foodEntry: IFoodEntry): Observable<EntityResponseType> {
    return this.http.put<IFoodEntry>(`${this.resourceUrl}/${getFoodEntryIdentifier(foodEntry) as number}`, foodEntry, {
      observe: 'response',
    });
  }

  partialUpdate(foodEntry: IFoodEntry): Observable<EntityResponseType> {
    return this.http.patch<IFoodEntry>(`${this.resourceUrl}/${getFoodEntryIdentifier(foodEntry) as number}`, foodEntry, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFoodEntry>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFoodEntry[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFoodEntryToCollectionIfMissing(
    foodEntryCollection: IFoodEntry[],
    ...foodEntriesToCheck: (IFoodEntry | null | undefined)[]
  ): IFoodEntry[] {
    const foodEntries: IFoodEntry[] = foodEntriesToCheck.filter(isPresent);
    if (foodEntries.length > 0) {
      const foodEntryCollectionIdentifiers = foodEntryCollection.map(foodEntryItem => getFoodEntryIdentifier(foodEntryItem)!);
      const foodEntriesToAdd = foodEntries.filter(foodEntryItem => {
        const foodEntryIdentifier = getFoodEntryIdentifier(foodEntryItem);
        if (foodEntryIdentifier == null || foodEntryCollectionIdentifiers.includes(foodEntryIdentifier)) {
          return false;
        }
        foodEntryCollectionIdentifiers.push(foodEntryIdentifier);
        return true;
      });
      return [...foodEntriesToAdd, ...foodEntryCollection];
    }
    return foodEntryCollection;
  }
}
