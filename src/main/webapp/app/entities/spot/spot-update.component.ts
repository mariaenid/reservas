import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ISpot } from 'app/shared/model/spot.model';
import { SpotService } from './spot.service';
import { IRestaurante } from 'app/shared/model/restaurante.model';
import { RestauranteService } from 'app/entities/restaurante';

@Component({
    selector: 'jhi-spot-update',
    templateUrl: './spot-update.component.html'
})
export class SpotUpdateComponent implements OnInit {
    spot: ISpot;
    isSaving: boolean;

    restaurantes: IRestaurante[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected spotService: SpotService,
        protected restauranteService: RestauranteService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ spot }) => {
            this.spot = spot;
        });
        this.restauranteService.query().subscribe(
            (res: HttpResponse<IRestaurante[]>) => {
                this.restaurantes = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.spot.id !== undefined) {
            this.subscribeToSaveResponse(this.spotService.update(this.spot));
        } else {
            this.subscribeToSaveResponse(this.spotService.create(this.spot));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISpot>>) {
        result.subscribe((res: HttpResponse<ISpot>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
}
