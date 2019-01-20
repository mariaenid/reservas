import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';

import { IReserva } from 'app/shared/model/reserva.model';
import { ReservaService } from './reserva.service';
import { IRestaurante } from 'app/shared/model/restaurante.model';
import { RestauranteService } from 'app/entities/restaurante';
import { ISpot } from 'app/shared/model/spot.model';
import { SpotService } from 'app/entities/spot';
import { ICliente } from 'app/shared/model/cliente.model';
import { ClienteService } from 'app/entities/cliente';

@Component({
    selector: 'jhi-reserva-update',
    templateUrl: './reserva-update.component.html'
})
export class ReservaUpdateComponent implements OnInit {
    reserva: IReserva;
    isSaving: boolean;

    restaurantes: IRestaurante[];

    spots: ISpot[];

    clientes: ICliente[];
    fechaDp: any;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected reservaService: ReservaService,
        protected restauranteService: RestauranteService,
        protected spotService: SpotService,
        protected clienteService: ClienteService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ reserva }) => {
            this.reserva = reserva;
        });
        this.restauranteService.query().subscribe(
            (res: HttpResponse<IRestaurante[]>) => {
                this.restaurantes = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.spotService.query().subscribe(
            (res: HttpResponse<ISpot[]>) => {
                this.spots = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.clienteService.query().subscribe(
            (res: HttpResponse<ICliente[]>) => {
                this.clientes = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.reserva.id !== undefined) {
            this.subscribeToSaveResponse(this.reservaService.update(this.reserva));
        } else {
            this.subscribeToSaveResponse(this.reservaService.create(this.reserva));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IReserva>>) {
        result.subscribe((res: HttpResponse<IReserva>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackRestauranteById(index: number, item: IRestaurante) {
        return item.id;
    }

    trackSpotById(index: number, item: ISpot) {
        return item.id;
    }

    trackClienteById(index: number, item: ICliente) {
        return item.id;
    }
}
