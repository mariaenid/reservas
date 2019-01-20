import { Moment } from 'moment';
import { IRestaurante } from 'app/shared/model//restaurante.model';
import { ISpot } from 'app/shared/model//spot.model';
import { ICliente } from 'app/shared/model//cliente.model';

export interface IReserva {
    id?: number;
    fecha?: Moment;
    restaurante?: IRestaurante;
    spot?: ISpot;
    cliente?: ICliente;
}

export class Reserva implements IReserva {
    constructor(
        public id?: number,
        public fecha?: Moment,
        public restaurante?: IRestaurante,
        public spot?: ISpot,
        public cliente?: ICliente
    ) {}
}
