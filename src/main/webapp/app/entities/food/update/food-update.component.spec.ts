jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FoodService } from '../service/food.service';
import { IFood, Food } from '../food.model';
import { IFoodEntry } from 'app/entities/food-entry/food-entry.model';
import { FoodEntryService } from 'app/entities/food-entry/service/food-entry.service';

import { FoodUpdateComponent } from './food-update.component';

describe('Component Tests', () => {
  describe('Food Management Update Component', () => {
    let comp: FoodUpdateComponent;
    let fixture: ComponentFixture<FoodUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let foodService: FoodService;
    let foodEntryService: FoodEntryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FoodUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FoodUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FoodUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      foodService = TestBed.inject(FoodService);
      foodEntryService = TestBed.inject(FoodEntryService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call FoodEntry query and add missing value', () => {
        const food: IFood = { id: 456 };
        const foodEntries: IFoodEntry[] = [{ id: 58139 }];
        food.foodEntries = foodEntries;

        const foodEntryCollection: IFoodEntry[] = [{ id: 14217 }];
        spyOn(foodEntryService, 'query').and.returnValue(of(new HttpResponse({ body: foodEntryCollection })));
        const additionalFoodEntries = [...foodEntries];
        const expectedCollection: IFoodEntry[] = [...additionalFoodEntries, ...foodEntryCollection];
        spyOn(foodEntryService, 'addFoodEntryToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ food });
        comp.ngOnInit();

        expect(foodEntryService.query).toHaveBeenCalled();
        expect(foodEntryService.addFoodEntryToCollectionIfMissing).toHaveBeenCalledWith(foodEntryCollection, ...additionalFoodEntries);
        expect(comp.foodEntriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const food: IFood = { id: 456 };
        const foodEntries: IFoodEntry = { id: 77395 };
        food.foodEntries = [foodEntries];

        activatedRoute.data = of({ food });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(food));
        expect(comp.foodEntriesSharedCollection).toContain(foodEntries);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const food = { id: 123 };
        spyOn(foodService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ food });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: food }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(foodService.update).toHaveBeenCalledWith(food);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const food = new Food();
        spyOn(foodService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ food });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: food }));
        saveSubject.complete();

        // THEN
        expect(foodService.create).toHaveBeenCalledWith(food);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const food = { id: 123 };
        spyOn(foodService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ food });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(foodService.update).toHaveBeenCalledWith(food);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackFoodEntryById', () => {
        it('Should return tracked FoodEntry primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFoodEntryById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedFoodEntry', () => {
        it('Should return option if no FoodEntry is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedFoodEntry(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected FoodEntry for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedFoodEntry(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this FoodEntry is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedFoodEntry(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
