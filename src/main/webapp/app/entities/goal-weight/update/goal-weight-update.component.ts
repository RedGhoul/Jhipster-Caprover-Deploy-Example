import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IGoalWeight, GoalWeight } from '../goal-weight.model';
import { GoalWeightService } from '../service/goal-weight.service';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';

@Component({
  selector: 'jhi-goal-weight-update',
  templateUrl: './goal-weight-update.component.html',
})
export class GoalWeightUpdateComponent implements OnInit {
  isSaving = false;

  appUsersSharedCollection: IAppUser[] = [];

  editForm = this.fb.group({
    id: [],
    weight: [null, [Validators.required]],
    createdDate: [],
    user: [],
  });

  constructor(
    protected goalWeightService: GoalWeightService,
    protected appUserService: AppUserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ goalWeight }) => {
      this.updateForm(goalWeight);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const goalWeight = this.createFromForm();
    if (goalWeight.id !== undefined) {
      this.subscribeToSaveResponse(this.goalWeightService.update(goalWeight));
    } else {
      this.subscribeToSaveResponse(this.goalWeightService.create(goalWeight));
    }
  }

  trackAppUserById(index: number, item: IAppUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGoalWeight>>): void {
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

  protected updateForm(goalWeight: IGoalWeight): void {
    this.editForm.patchValue({
      id: goalWeight.id,
      weight: goalWeight.weight,
      createdDate: goalWeight.createdDate,
      user: goalWeight.user,
    });

    this.appUsersSharedCollection = this.appUserService.addAppUserToCollectionIfMissing(this.appUsersSharedCollection, goalWeight.user);
  }

  protected loadRelationshipsOptions(): void {
    this.appUserService
      .query()
      .pipe(map((res: HttpResponse<IAppUser[]>) => res.body ?? []))
      .pipe(map((appUsers: IAppUser[]) => this.appUserService.addAppUserToCollectionIfMissing(appUsers, this.editForm.get('user')!.value)))
      .subscribe((appUsers: IAppUser[]) => (this.appUsersSharedCollection = appUsers));
  }

  protected createFromForm(): IGoalWeight {
    return {
      ...new GoalWeight(),
      id: this.editForm.get(['id'])!.value,
      weight: this.editForm.get(['weight'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
