import { Moment } from 'moment';
import { ISpot } from 'app/shared/model//spot.model';
import { IReserva } from 'app/shared/model//reserva.model';

export interface IRestaurante {
    id?: number;
    nombre?: string;
    fecha?: Moment;
    propietario?: string;
    direccion?: string;
    imagenContentType?: string;
    imagen?: any;
    resspots?: ISpot[];
    spotreservas?: IReserva[];
}

export class Restaurante implements IRestaurante {
    constructor(
        public id?: number,
        public nombre?: string,
        public fecha?: Moment,
        public propietario?: string,
        public direccion?: string,
        public imagenContentType?: string,
        public imagen?: any,
        public resspots?: ISpot[],
        public spotreservas?: IReserva[]
    ) {}
}
