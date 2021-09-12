import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFood, Food } from '../food.model';
import { FoodService } from '../service/food.service';
import { IFoodEntry } from 'app/entities/food-entry/food-entry.model';
import { FoodEntryService } from 'app/entities/food-entry/service/food-entry.service';

@Component({
  selector: 'jhi-food-update',
  templateUrl: './food-update.component.html',
})
export class FoodUpdateComponent implements OnInit {
  isSaving = false;

  foodEntriesSharedCollection: IFoodEntry[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.minLength(2)]],
    calories: [null, [Validators.required]],
    carbohydrates: [null, [Validators.required]],
    proteins: [null, [Validators.required]],
    fat: [null, [Validators.required]],
    sodium: [null, [Validators.required]],
    foodEntries: [],
  });

  constructor(
    protected foodService: FoodService,
    protected foodEntryService: FoodEntryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ food }) => {
      this.updateForm(food);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const food = this.createFromForm();
    if (food.id !== undefined) {
      this.subscribeToSaveResponse(this.foodService.update(food));
    } else {
      this.subscribeToSaveResponse(this.foodService.create(food));
    }
  }

  trackFoodEntryById(index: number, item: IFoodEntry): number {
    return item.id!;
  }

  getSelectedFoodEntry(option: IFoodEntry, selectedVals?: IFoodEntry[]): IFoodEntry {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFood>>): void {
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

  protected updateForm(food: IFood): void {
    this.editForm.patchValue({
      id: food.id,
      name: food.name,
      calories: food.calories,
      carbohydrates: food.carbohydrates,
      proteins: food.proteins,
      fat: food.fat,
      sodium: food.sodium,
      foodEntries: food.foodEntries,
    });

    this.foodEntriesSharedCollection = this.foodEntryService.addFoodEntryToCollectionIfMissing(
      this.foodEntriesSharedCollection,
      ...(food.foodEntries ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.foodEntryService
      .query()
      .pipe(map((res: HttpResponse<IFoodEntry[]>) => res.body ?? []))
      .pipe(
        map((foodEntries: IFoodEntry[]) =>
          this.foodEntryService.addFoodEntryToCollectionIfMissing(foodEntries, ...(this.editForm.get('foodEntries')!.value ?? []))
        )
      )
      .subscribe((foodEntries: IFoodEntry[]) => (this.foodEntriesSharedCollection = foodEntries));
  }

  protected createFromForm(): IFood {
    return {
      ...new Food(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      calories: this.editForm.get(['calories'])!.value,
      carbohydrates: this.editForm.get(['carbohydrates'])!.value,
      proteins: this.editForm.get(['proteins'])!.value,
      fat: this.editForm.get(['fat'])!.value,
      sodium: this.editForm.get(['sodium'])!.value,
      foodEntries: this.editForm.get(['foodEntries'])!.value,
    };
  }
}
