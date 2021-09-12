import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFoodDay, FoodDay } from '../food-day.model';
import { FoodDayService } from '../service/food-day.service';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';

@Component({
  selector: 'jhi-food-day-update',
  templateUrl: './food-day-update.component.html',
})
export class FoodDayUpdateComponent implements OnInit {
  isSaving = false;

  appUsersSharedCollection: IAppUser[] = [];

  editForm = this.fb.group({
    id: [],
    createdDate: [],
    user: [],
  });

  constructor(
    protected foodDayService: FoodDayService,
    protected appUserService: AppUserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ foodDay }) => {
      this.updateForm(foodDay);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const foodDay = this.createFromForm();
    if (foodDay.id !== undefined) {
      this.subscribeToSaveResponse(this.foodDayService.update(foodDay));
    } else {
      this.subscribeToSaveResponse(this.foodDayService.create(foodDay));
    }
  }

  trackAppUserById(index: number, item: IAppUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFoodDay>>): void {
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

  protected updateForm(foodDay: IFoodDay): void {
    this.editForm.patchValue({
      id: foodDay.id,
      createdDate: foodDay.createdDate,
      user: foodDay.user,
    });

    this.appUsersSharedCollection = this.appUserService.addAppUserToCollectionIfMissing(this.appUsersSharedCollection, foodDay.user);
  }

  protected loadRelationshipsOptions(): void {
    this.appUserService
      .query()
      .pipe(map((res: HttpResponse<IAppUser[]>) => res.body ?? []))
      .pipe(map((appUsers: IAppUser[]) => this.appUserService.addAppUserToCollectionIfMissing(appUsers, this.editForm.get('user')!.value)))
      .subscribe((appUsers: IAppUser[]) => (this.appUsersSharedCollection = appUsers));
  }

  protected createFromForm(): IFoodDay {
    return {
      ...new FoodDay(),
      id: this.editForm.get(['id'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
