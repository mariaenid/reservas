import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { JhiDataUtils } from 'ng-jhipster';

import { IRestaurante } from 'app/shared/model/restaurante.model';
import { RestauranteService } from './restaurante.service';

@Component({
    selector: 'jhi-restaurante-update',
    templateUrl: './restaurante-update.component.html'
})
export class RestauranteUpdateComponent implements OnInit {
    restaurante: IRestaurante;
    isSaving: boolean;
    fechaDp: any;

    constructor(
        protected dataUtils: JhiDataUtils,
        protected restauranteService: RestauranteService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ restaurante }) => {
            this.restaurante = restaurante;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.restaurante.id !== undefined) {
            this.subscribeToSaveResponse(this.restauranteService.update(this.restaurante));
        } else {
            this.subscribeToSaveResponse(this.restauranteService.create(this.restaurante));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IRestaurante>>) {
        result.subscribe((res: HttpResponse<IRestaurante>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
