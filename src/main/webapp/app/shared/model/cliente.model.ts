import { IReserva } from 'app/shared/model//reserva.model';

export interface ICliente {
    id?: number;
    nombres?: string;
    correo?: string;
    clientereservas?: IReserva[];
}

export class Cliente implements ICliente {
    constructor(public id?: number, public nombres?: string, public correo?: string, public clientereservas?: IReserva[]) {}
}
