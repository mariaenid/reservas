import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISpot } from 'app/shared/model/spot.model';

type EntityResponseType = HttpResponse<ISpot>;
type EntityArrayResponseType = HttpResponse<ISpot[]>;

@Injectable({ providedIn: 'root' })
export class SpotService {
    public resourceUrl = SERVER_API_URL + 'api/spots';

    constructor(protected http: HttpClient) {}

    create(spot: ISpot): Observable<EntityResponseType> {
        return this.http.post<ISpot>(this.resourceUrl, spot, { observe: 'response' });
    }

    update(spot: ISpot): Observable<EntityResponseType> {
        return this.http.put<ISpot>(this.resourceUrl, spot, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISpot>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    findByRestauranteId(id: number): Observable<EntityArrayResponseType> {
        const options = createRequestOption(id);
        const res = SERVER_API_URL + 'api/spotsRestaurante';
        return this.http.get<ISpot[]>(`${res}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISpot[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
