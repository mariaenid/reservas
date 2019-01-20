import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Spot } from 'app/shared/model/spot.model';
import { SpotService } from './spot.service';
import { SpotComponent } from './spot.component';
import { SpotDetailComponent } from './spot-detail.component';
import { SpotUpdateComponent } from './spot-update.component';
import { SpotDeletePopupComponent } from './spot-delete-dialog.component';
import { ISpot } from 'app/shared/model/spot.model';

@Injectable({ providedIn: 'root' })
export class SpotResolve implements Resolve<ISpot> {
    constructor(private service: SpotService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Spot> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Spot>) => response.ok),
                map((spot: HttpResponse<Spot>) => spot.body)
            );
        }
        return of(new Spot());
    }
}

export const spotRoute: Routes = [
    {
        path: 'spot',
        component: SpotComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'reservasApp.spot.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'spot/:id/view',
        component: SpotDetailComponent,
        resolve: {
            spot: SpotResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reservasApp.spot.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'spot/new',
        component: SpotUpdateComponent,
        resolve: {
            spot: SpotResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reservasApp.spot.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'spot/:id/edit',
        component: SpotUpdateComponent,
        resolve: {
            spot: SpotResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reservasApp.spot.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const spotPopupRoute: Routes = [
    {
        path: 'spot/:id/delete',
        component: SpotDeletePopupComponent,
        resolve: {
            spot: SpotResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reservasApp.spot.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
