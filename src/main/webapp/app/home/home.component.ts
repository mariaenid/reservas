import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import * as moment from 'moment';

import { LoginModalService, AccountService, Account } from 'app/core';
import { JhiEventManager, JhiParseLinks, JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { Subscription, Observable } from 'rxjs';

import { RestauranteService } from '../entities/restaurante/restaurante.service';
import { SpotService } from '../entities/spot/spot.service';
import { ClienteService } from '../entities/cliente/cliente.service';
import { ReservaService } from '../entities/reserva/reserva.service';

import { IRestaurante } from 'app/shared/model/restaurante.model';
import { ISpot } from 'app/shared/model/spot.model';
import { ICliente } from 'app/shared/model/cliente.model';
import { IReserva } from 'app/shared/model/reserva.model';

import { USUARIO } from 'app/app.constants';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.css']
})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;

    private catalogInstance;
    private restaurantsMock: any;

    currentAccount: any;
    restaurantes: IRestaurante[];
    spots: ISpot[];
    cliente: ICliente;
    public currentRestaurant: IRestaurante;

    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;

    isSaving: boolean;

    constructor(
        protected parseLinks: JhiParseLinks,
        private restauranteService: RestauranteService,
        private spotService: SpotService,
        private reservaService: ReservaService,
        private clienteService: ClienteService,
        private accountService: AccountService,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        protected jhiAlertService: JhiAlertService
    ) {}

    loadAll() {
        this.restauranteService
            .findAll()
            .subscribe(
                (res: HttpResponse<IRestaurante[]>) => this.paginateRestaurantes(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    async viewRestaurant() {
        // console.log("DATASS");
        // this.respuesta = this.restauranteService.findAll();
        // console.log("Esta es la informacion amigo", this.respuesta);
    }

    ngOnInit() {
        this.isSaving = false;

        this.loadAll();
        this.registerChangeInRestaurantes();

        console.log('shaha', this.restaurantes);

        this.accountService.identity().then(account => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
        // this.restaurantService.getall();
        this.viewRestaurant();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.accountService.identity().then(account => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.accountService.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    public async viewRestaurantSpots(r) {
        console.log('Hello');
        this.currentRestaurant = r;
        this.findByRestauranteID(r.id);
    }

    public async cancel() {
        this.currentRestaurant = null;
    }

    protected paginateRestaurantes(data: IRestaurante[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.restaurantes = data;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    registerChangeInRestaurantes() {
        this.eventSubscriber = this.eventManager.subscribe('restauranteListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    findByRestauranteID(idRestaurante: number) {
        this.spotService
            .findByRestauranteId(idRestaurante)
            .subscribe(
                (res: HttpResponse<ISpot[]>) => this.paginateSpots(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    protected paginateSpots(data: ISpot[], headers: HttpHeaders) {
        // this.links = this.parseLinks.parse(headers.get('link'));
        // this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        // this.queryCount = this.totalItems;
        console.log('dagats', data);
        this.spots = data;
    }

    protected paginateCliente(data: ICliente, headers: HttpHeaders) {
        // this.links = this.parseLinks.parse(headers.get('link'));
        // this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        // this.queryCount = this.totalItems;

        this.cliente = data;
    }

    save(reserva: IReserva) {
        console.log('Esta es una reserva', reserva);

        this.isSaving = true;
        if (reserva.id !== undefined) {
            this.subscribeToSaveResponse(this.reservaService.update(reserva));
        } else {
            this.subscribeToSaveResponse(this.reservaService.create(reserva));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IReserva>>) {
        result.subscribe((res: HttpResponse<IReserva>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    previousState() {
        window.history.back();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    saveReserve(spot) {
        const fechaActual = moment();

        console.log('DEberias guardar una reserva amigoo', spot, this.currentRestaurant);
        this.clienteService
            .find(USUARIO)
            .subscribe(
                (res: HttpResponse<ICliente>) => this.paginateCliente(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );

        console.log('cliente', this.cliente);
        const reserva = { cliente: this.cliente, restaurante: this.currentRestaurant, spot, fecha: fechaActual };
        console.log('Informacion dada', reserva);
        this.save(reserva);
    }
}
