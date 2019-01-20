import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReservasSharedModule } from 'app/shared';
import {
    ReservaComponent,
    ReservaDetailComponent,
    ReservaUpdateComponent,
    ReservaDeletePopupComponent,
    ReservaDeleteDialogComponent,
    reservaRoute,
    reservaPopupRoute
} from './';

const ENTITY_STATES = [...reservaRoute, ...reservaPopupRoute];

@NgModule({
    imports: [ReservasSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ReservaComponent,
        ReservaDetailComponent,
        ReservaUpdateComponent,
        ReservaDeleteDialogComponent,
        ReservaDeletePopupComponent
    ],
    entryComponents: [ReservaComponent, ReservaUpdateComponent, ReservaDeleteDialogComponent, ReservaDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReservasReservaModule {}
