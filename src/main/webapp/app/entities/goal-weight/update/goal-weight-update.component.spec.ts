jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { GoalWeightService } from '../service/goal-weight.service';
import { IGoalWeight, GoalWeight } from '../goal-weight.model';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';

import { GoalWeightUpdateComponent } from './goal-weight-update.component';

describe('Component Tests', () => {
  describe('GoalWeight Management Update Component', () => {
    let comp: GoalWeightUpdateComponent;
    let fixture: ComponentFixture<GoalWeightUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let goalWeightService: GoalWeightService;
    let appUserService: AppUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [GoalWeightUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(GoalWeightUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GoalWeightUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      goalWeightService = TestBed.inject(GoalWeightService);
      appUserService = TestBed.inject(AppUserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call AppUser query and add missing value', () => {
        const goalWeight: IGoalWeight = { id: 456 };
        const user: IAppUser = { id: 8446 };
        goalWeight.user = user;

        const appUserCollection: IAppUser[] = [{ id: 25755 }];
        spyOn(appUserService, 'query').and.returnValue(of(new HttpResponse({ body: appUserCollection })));
        const additionalAppUsers = [user];
        const expectedCollection: IAppUser[] = [...additionalAppUsers, ...appUserCollection];
        spyOn(appUserService, 'addAppUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ goalWeight });
        comp.ngOnInit();

        expect(appUserService.query).toHaveBeenCalled();
        expect(appUserService.addAppUserToCollectionIfMissing).toHaveBeenCalledWith(appUserCollection, ...additionalAppUsers);
        expect(comp.appUsersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const goalWeight: IGoalWeight = { id: 456 };
        const user: IAppUser = { id: 95068 };
        goalWeight.user = user;

        activatedRoute.data = of({ goalWeight });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(goalWeight));
        expect(comp.appUsersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const goalWeight = { id: 123 };
        spyOn(goalWeightService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ goalWeight });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: goalWeight }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(goalWeightService.update).toHaveBeenCalledWith(goalWeight);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const goalWeight = new GoalWeight();
        spyOn(goalWeightService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ goalWeight });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: goalWeight }));
        saveSubject.complete();

        // THEN
        expect(goalWeightService.create).toHaveBeenCalledWith(goalWeight);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const goalWeight = { id: 123 };
        spyOn(goalWeightService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ goalWeight });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(goalWeightService.update).toHaveBeenCalledWith(goalWeight);
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
