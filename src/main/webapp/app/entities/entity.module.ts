import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ReservasRestauranteModule } from './restaurante/restaurante.module';
import { ReservasSpotModule } from './spot/spot.module';
import { ReservasReservaModule } from './reserva/reserva.module';
import { ReservasClienteModule } from './cliente/cliente.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        ReservasRestauranteModule,
        ReservasSpotModule,
        ReservasReservaModule,
        ReservasClienteModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReservasEntityModule {}
