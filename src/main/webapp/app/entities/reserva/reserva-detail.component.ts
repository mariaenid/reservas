import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReserva } from 'app/shared/model/reserva.model';

@Component({
    selector: 'jhi-reserva-detail',
    templateUrl: './reserva-detail.component.html'
})
export class ReservaDetailComponent implements OnInit {
    reserva: IReserva;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ reserva }) => {
            this.reserva = reserva;
        });
    }

    previousState() {
        window.history.back();
    }
}
