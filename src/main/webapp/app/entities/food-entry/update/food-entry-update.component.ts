import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFoodEntry, FoodEntry } from '../food-entry.model';
import { FoodEntryService } from '../service/food-entry.service';
import { IFoodDay } from 'app/entities/food-day/food-day.model';
import { FoodDayService } from 'app/entities/food-day/service/food-day.service';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';

@Component({
  selector: 'jhi-food-entry-update',
  templateUrl: './food-entry-update.component.html',
})
export class FoodEntryUpdateComponent implements OnInit {
  isSaving = false;

  foodDaysSharedCollection: IFoodDay[] = [];
  appUsersSharedCollection: IAppUser[] = [];

  editForm = this.fb.group({
    id: [],
    mealtype: [],
    foodDay: [],
    user: [],
  });

  constructor(
    protected foodEntryService: FoodEntryService,
    protected foodDayService: FoodDayService,
    protected appUserService: AppUserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ foodEntry }) => {
      this.updateForm(foodEntry);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const foodEntry = this.createFromForm();
    if (foodEntry.id !== undefined) {
      this.subscribeToSaveResponse(this.foodEntryService.update(foodEntry));
    } else {
      this.subscribeToSaveResponse(this.foodEntryService.create(foodEntry));
    }
  }

  trackFoodDayById(index: number, item: IFoodDay): number {
    return item.id!;
  }

  trackAppUserById(index: number, item: IAppUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFoodEntry>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(foodEntry: IFoodEntry): void {
    this.editForm.patchValue({
      id: foodEntry.id,
      mealtype: foodEntry.mealtype,
      foodDay: foodEntry.foodDay,
      user: foodEntry.user,
    });

    this.foodDaysSharedCollection = this.foodDayService.addFoodDayToCollectionIfMissing(this.foodDaysSharedCollection, foodEntry.foodDay);
    this.appUsersSharedCollection = this.appUserService.addAppUserToCollectionIfMissing(this.appUsersSharedCollection, foodEntry.user);
  }

  protected loadRelationshipsOptions(): void {
    this.foodDayService
      .query()
      .pipe(map((res: HttpResponse<IFoodDay[]>) => res.body ?? []))
      .pipe(
        map((foodDays: IFoodDay[]) => this.foodDayService.addFoodDayToCollectionIfMissing(foodDays, this.editForm.get('foodDay')!.value))
      )
      .subscribe((foodDays: IFoodDay[]) => (this.foodDaysSharedCollection = foodDays));

    this.appUserService
      .query()
      .pipe(map((res: HttpResponse<IAppUser[]>) => res.body ?? []))
      .pipe(map((appUsers: IAppUser[]) => this.appUserService.addAppUserToCollectionIfMissing(appUsers, this.editForm.get('user')!.value)))
      .subscribe((appUsers: IAppUser[]) => (this.appUsersSharedCollection = appUsers));
  }

  protected createFromForm(): IFoodEntry {
    return {
      ...new FoodEntry(),
      id: this.editForm.get(['id'])!.value,
      mealtype: this.editForm.get(['mealtype'])!.value,
      foodDay: this.editForm.get(['foodDay'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
