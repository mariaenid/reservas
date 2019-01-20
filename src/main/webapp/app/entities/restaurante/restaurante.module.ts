import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReservasSharedModule } from 'app/shared';
import {
    RestauranteComponent,
    RestauranteDetailComponent,
    RestauranteUpdateComponent,
    RestauranteDeletePopupComponent,
    RestauranteDeleteDialogComponent,
    restauranteRoute,
    restaurantePopupRoute
} from './';

const ENTITY_STATES = [...restauranteRoute, ...restaurantePopupRoute];

@NgModule({
    imports: [ReservasSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        RestauranteComponent,
        RestauranteDetailComponent,
        RestauranteUpdateComponent,
        RestauranteDeleteDialogComponent,
        RestauranteDeletePopupComponent
    ],
    entryComponents: [RestauranteComponent, RestauranteUpdateComponent, RestauranteDeleteDialogComponent, RestauranteDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReservasRestauranteModule {}
