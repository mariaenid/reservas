/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ReservasTestModule } from '../../../test.module';
import { SpotDeleteDialogComponent } from 'app/entities/spot/spot-delete-dialog.component';
import { SpotService } from 'app/entities/spot/spot.service';

describe('Component Tests', () => {
    describe('Spot Management Delete Component', () => {
        let comp: SpotDeleteDialogComponent;
        let fixture: ComponentFixture<SpotDeleteDialogComponent>;
        let service: SpotService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReservasTestModule],
                declarations: [SpotDeleteDialogComponent]
            })
                .overrideTemplate(SpotDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SpotDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SpotService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
