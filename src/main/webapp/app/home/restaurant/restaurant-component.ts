import { Component, OnInit, OnDestroy } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiParseLinks, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { Input, EventEmitter, Output } from '@angular/core';

import { IRestaurante } from 'app/shared/model/restaurante.model';
import { ISpot } from 'app/shared/model/spot.model';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';

import { SpotService } from 'app/entities/spot/spot.service';

import { LoginModalService, AccountService, Account } from 'app/core';
import { Subscription } from 'rxjs';

@Component({
    selector: 'restaurant-component',
    templateUrl: './restaurant-component.html',
    styleUrls: ['./restaurant-component.css']
})
export class RestaurantComponent implements OnInit {
    @Input() public restaurant: IRestaurante;
    @Output() public onRestaurantClick: EventEmitter<any> = new EventEmitter();

    currentAccount: any;
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

    constructor(
        protected dataUtils: JhiDataUtils,
        protected parseLinks: JhiParseLinks,
        protected jhiAlertService: JhiAlertService,
        protected accountService: AccountService,
        protected eventManager: JhiEventManager
    ) {}

    ngOnInit() {
        console.log('INFORMACTION', this.restaurant);
    }

    public viewRestaurant() {
        console.log('Esto es un click', this.restaurant.id);
        this.onRestaurantClick.emit(this.restaurant);
        // this.findByRestauranteID(this.restaurant.id);
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
