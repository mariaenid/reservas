/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ReservasTestModule } from '../../../test.module';
import { SpotUpdateComponent } from 'app/entities/spot/spot-update.component';
import { SpotService } from 'app/entities/spot/spot.service';
import { Spot } from 'app/shared/model/spot.model';

describe('Component Tests', () => {
    describe('Spot Management Update Component', () => {
        let comp: SpotUpdateComponent;
        let fixture: ComponentFixture<SpotUpdateComponent>;
        let service: SpotService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReservasTestModule],
                declarations: [SpotUpdateComponent]
            })
                .overrideTemplate(SpotUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SpotUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SpotService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Spot(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.spot = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Spot();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.spot = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
