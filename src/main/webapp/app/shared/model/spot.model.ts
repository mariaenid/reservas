import { IRestaurante } from 'app/shared/model//restaurante.model';
import { IReserva } from 'app/shared/model//reserva.model';

export interface ISpot {
    id?: number;
    numPersonas?: number;
    numMesa?: number;
    restaurante?: IRestaurante;
    spotreservas?: IReserva[];
}

export class Spot implements ISpot {
    constructor(
        public id?: number,
        public numPersonas?: number,
        public numMesa?: number,
        public restaurante?: IRestaurante,
        public spotreservas?: IReserva[]
    ) {}
}
