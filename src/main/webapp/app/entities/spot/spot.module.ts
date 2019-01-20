import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReservasSharedModule } from 'app/shared';
import {
    SpotComponent,
    SpotDetailComponent,
    SpotUpdateComponent,
    SpotDeletePopupComponent,
    SpotDeleteDialogComponent,
    spotRoute,
    spotPopupRoute
} from './';

const ENTITY_STATES = [...spotRoute, ...spotPopupRoute];

@NgModule({
    imports: [ReservasSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [SpotComponent, SpotDetailComponent, SpotUpdateComponent, SpotDeleteDialogComponent, SpotDeletePopupComponent],
    entryComponents: [SpotComponent, SpotUpdateComponent, SpotDeleteDialogComponent, SpotDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReservasSpotModule {}
