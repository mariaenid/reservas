/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReservasTestModule } from '../../../test.module';
import { SpotDetailComponent } from 'app/entities/spot/spot-detail.component';
import { Spot } from 'app/shared/model/spot.model';

describe('Component Tests', () => {
    describe('Spot Management Detail Component', () => {
        let comp: SpotDetailComponent;
        let fixture: ComponentFixture<SpotDetailComponent>;
        const route = ({ data: of({ spot: new Spot(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReservasTestModule],
                declarations: [SpotDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SpotDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SpotDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.spot).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
