jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CurrentWeightService } from '../service/current-weight.service';
import { ICurrentWeight, CurrentWeight } from '../current-weight.model';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';

import { CurrentWeightUpdateComponent } from './current-weight-update.component';

describe('Component Tests', () => {
  describe('CurrentWeight Management Update Component', () => {
    let comp: CurrentWeightUpdateComponent;
    let fixture: ComponentFixture<CurrentWeightUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let currentWeightService: CurrentWeightService;
    let appUserService: AppUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CurrentWeightUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CurrentWeightUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CurrentWeightUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      currentWeightService = TestBed.inject(CurrentWeightService);
      appUserService = TestBed.inject(AppUserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call AppUser query and add missing value', () => {
        const currentWeight: ICurrentWeight = { id: 456 };
        const user: IAppUser = { id: 27496 };
        currentWeight.user = user;

        const appUserCollection: IAppUser[] = [{ id: 13064 }];
        spyOn(appUserService, 'query').and.returnValue(of(new HttpResponse({ body: appUserCollection })));
        const additionalAppUsers = [user];
        const expectedCollection: IAppUser[] = [...additionalAppUsers, ...appUserCollection];
        spyOn(appUserService, 'addAppUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ currentWeight });
        comp.ngOnInit();

        expect(appUserService.query).toHaveBeenCalled();
        expect(appUserService.addAppUserToCollectionIfMissing).toHaveBeenCalledWith(appUserCollection, ...additionalAppUsers);
        expect(comp.appUsersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const currentWeight: ICurrentWeight = { id: 456 };
        const user: IAppUser = { id: 28360 };
        currentWeight.user = user;

        activatedRoute.data = of({ currentWeight });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(currentWeight));
        expect(comp.appUsersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const currentWeight = { id: 123 };
        spyOn(currentWeightService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ currentWeight });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: currentWeight }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(currentWeightService.update).toHaveBeenCalledWith(currentWeight);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const currentWeight = new CurrentWeight();
        spyOn(currentWeightService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ currentWeight });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: currentWeight }));
        saveSubject.complete();

        // THEN
        expect(currentWeightService.create).toHaveBeenCalledWith(currentWeight);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const currentWeight = { id: 123 };
        spyOn(currentWeightService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ currentWeight });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(currentWeightService.update).toHaveBeenCalledWith(currentWeight);
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
