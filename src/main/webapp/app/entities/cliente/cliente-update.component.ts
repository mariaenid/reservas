import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ICliente } from 'app/shared/model/cliente.model';
import { ClienteService } from './cliente.service';

@Component({
    selector: 'jhi-cliente-update',
    templateUrl: './cliente-update.component.html'
})
export class ClienteUpdateComponent implements OnInit {
    cliente: ICliente;
    isSaving: boolean;

    constructor(protected clienteService: ClienteService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ cliente }) => {
            this.cliente = cliente;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.cliente.id !== undefined) {
            this.subscribeToSaveResponse(this.clienteService.update(this.cliente));
        } else {
            this.subscribeToSaveResponse(this.clienteService.create(this.cliente));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ICliente>>) {
        result.subscribe((res: HttpResponse<ICliente>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
