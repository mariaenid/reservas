import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReservasSharedModule } from 'app/shared';

import { HOME_ROUTE, HomeComponent } from './';

import { RestaurantComponent, SpotComponent } from './';

@NgModule({
    imports: [ReservasSharedModule, RouterModule.forChild([HOME_ROUTE])],
    declarations: [HomeComponent, RestaurantComponent, SpotComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReservasHomeModule {}
