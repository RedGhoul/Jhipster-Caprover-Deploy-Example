jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FoodDayService } from '../service/food-day.service';
import { IFoodDay, FoodDay } from '../food-day.model';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';

import { FoodDayUpdateComponent } from './food-day-update.component';

describe('Component Tests', () => {
  describe('FoodDay Management Update Component', () => {
    let comp: FoodDayUpdateComponent;
    let fixture: ComponentFixture<FoodDayUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let foodDayService: FoodDayService;
    let appUserService: AppUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FoodDayUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FoodDayUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FoodDayUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      foodDayService = TestBed.inject(FoodDayService);
      appUserService = TestBed.inject(AppUserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call AppUser query and add missing value', () => {
        const foodDay: IFoodDay = { id: 456 };
        const user: IAppUser = { id: 66276 };
        foodDay.user = user;

        const appUserCollection: IAppUser[] = [{ id: 86447 }];
        spyOn(appUserService, 'query').and.returnValue(of(new HttpResponse({ body: appUserCollection })));
        const additionalAppUsers = [user];
        const expectedCollection: IAppUser[] = [...additionalAppUsers, ...appUserCollection];
        spyOn(appUserService, 'addAppUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ foodDay });
        comp.ngOnInit();

        expect(appUserService.query).toHaveBeenCalled();
        expect(appUserService.addAppUserToCollectionIfMissing).toHaveBeenCalledWith(appUserCollection, ...additionalAppUsers);
        expect(comp.appUsersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const foodDay: IFoodDay = { id: 456 };
        const user: IAppUser = { id: 32207 };
        foodDay.user = user;

        activatedRoute.data = of({ foodDay });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(foodDay));
        expect(comp.appUsersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const foodDay = { id: 123 };
        spyOn(foodDayService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ foodDay });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: foodDay }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(foodDayService.update).toHaveBeenCalledWith(foodDay);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const foodDay = new FoodDay();
        spyOn(foodDayService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ foodDay });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: foodDay }));
        saveSubject.complete();

        // THEN
        expect(foodDayService.create).toHaveBeenCalledWith(foodDay);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const foodDay = { id: 123 };
        spyOn(foodDayService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ foodDay });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(foodDayService.update).toHaveBeenCalledWith(foodDay);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
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
