/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReservasTestModule } from '../../../test.module';
import { ReservaDetailComponent } from 'app/entities/reserva/reserva-detail.component';
import { Reserva } from 'app/shared/model/reserva.model';

describe('Component Tests', () => {
    describe('Reserva Management Detail Component', () => {
        let comp: ReservaDetailComponent;
        let fixture: ComponentFixture<ReservaDetailComponent>;
        const route = ({ data: of({ reserva: new Reserva(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReservasTestModule],
                declarations: [ReservaDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ReservaDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ReservaDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.reserva).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
