jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FoodEntryService } from '../service/food-entry.service';
import { IFoodEntry, FoodEntry } from '../food-entry.model';
import { IFoodDay } from 'app/entities/food-day/food-day.model';
import { FoodDayService } from 'app/entities/food-day/service/food-day.service';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';

import { FoodEntryUpdateComponent } from './food-entry-update.component';

describe('Component Tests', () => {
  describe('FoodEntry Management Update Component', () => {
    let comp: FoodEntryUpdateComponent;
    let fixture: ComponentFixture<FoodEntryUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let foodEntryService: FoodEntryService;
    let foodDayService: FoodDayService;
    let appUserService: AppUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FoodEntryUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FoodEntryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FoodEntryUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      foodEntryService = TestBed.inject(FoodEntryService);
      foodDayService = TestBed.inject(FoodDayService);
      appUserService = TestBed.inject(AppUserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call FoodDay query and add missing value', () => {
        const foodEntry: IFoodEntry = { id: 456 };
        const foodDay: IFoodDay = { id: 33027 };
        foodEntry.foodDay = foodDay;

        const foodDayCollection: IFoodDay[] = [{ id: 40814 }];
        spyOn(foodDayService, 'query').and.returnValue(of(new HttpResponse({ body: foodDayCollection })));
        const additionalFoodDays = [foodDay];
        const expectedCollection: IFoodDay[] = [...additionalFoodDays, ...foodDayCollection];
        spyOn(foodDayService, 'addFoodDayToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ foodEntry });
        comp.ngOnInit();

        expect(foodDayService.query).toHaveBeenCalled();
        expect(foodDayService.addFoodDayToCollectionIfMissing).toHaveBeenCalledWith(foodDayCollection, ...additionalFoodDays);
        expect(comp.foodDaysSharedCollection).toEqual(expectedCollection);
      });

      it('Should call AppUser query and add missing value', () => {
        const foodEntry: IFoodEntry = { id: 456 };
        const user: IAppUser = { id: 92557 };
        foodEntry.user = user;

        const appUserCollection: IAppUser[] = [{ id: 69823 }];
        spyOn(appUserService, 'query').and.returnValue(of(new HttpResponse({ body: appUserCollection })));
        const additionalAppUsers = [user];
        const expectedCollection: IAppUser[] = [...additionalAppUsers, ...appUserCollection];
        spyOn(appUserService, 'addAppUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ foodEntry });
        comp.ngOnInit();

        expect(appUserService.query).toHaveBeenCalled();
        expect(appUserService.addAppUserToCollectionIfMissing).toHaveBeenCalledWith(appUserCollection, ...additionalAppUsers);
        expect(comp.appUsersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const foodEntry: IFoodEntry = { id: 456 };
        const foodDay: IFoodDay = { id: 29770 };
        foodEntry.foodDay = foodDay;
        const user: IAppUser = { id: 14985 };
        foodEntry.user = user;

        activatedRoute.data = of({ foodEntry });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(foodEntry));
        expect(comp.foodDaysSharedCollection).toContain(foodDay);
        expect(comp.appUsersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const foodEntry = { id: 123 };
        spyOn(foodEntryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ foodEntry });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: foodEntry }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(foodEntryService.update).toHaveBeenCalledWith(foodEntry);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const foodEntry = new FoodEntry();
        spyOn(foodEntryService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ foodEntry });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: foodEntry }));
        saveSubject.complete();

        // THEN
        expect(foodEntryService.create).toHaveBeenCalledWith(foodEntry);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const foodEntry = { id: 123 };
        spyOn(foodEntryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ foodEntry });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(foodEntryService.update).toHaveBeenCalledWith(foodEntry);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackFoodDayById', () => {
        it('Should return tracked FoodDay primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFoodDayById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackAppUserById', () => {
        it('Should return tracked AppUser primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAppUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
