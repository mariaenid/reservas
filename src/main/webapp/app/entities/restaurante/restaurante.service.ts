import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRestaurante } from 'app/shared/model/restaurante.model';

type EntityResponseType = HttpResponse<IRestaurante>;
type EntityArrayResponseType = HttpResponse<IRestaurante[]>;

@Injectable({ providedIn: 'root' })
export class RestauranteService {
    public resourceUrl = SERVER_API_URL + 'api/restaurantes';

    constructor(protected http: HttpClient) {}

    create(restaurante: IRestaurante): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(restaurante);
        return this.http
            .post<IRestaurante>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(restaurante: IRestaurante): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(restaurante);
        return this.http
            .put<IRestaurante>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IRestaurante>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IRestaurante[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(restaurante: IRestaurante): IRestaurante {
        const copy: IRestaurante = Object.assign({}, restaurante, {
            fecha: restaurante.fecha != null && restaurante.fecha.isValid() ? restaurante.fecha.format(DATE_FORMAT) : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.fecha = res.body.fecha != null ? moment(res.body.fecha) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((restaurante: IRestaurante) => {
                restaurante.fecha = restaurante.fecha != null ? moment(restaurante.fecha) : null;
            });
        }
        return res;
    }
}
