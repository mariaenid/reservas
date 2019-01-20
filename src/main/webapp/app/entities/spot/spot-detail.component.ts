import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISpot } from 'app/shared/model/spot.model';

@Component({
    selector: 'jhi-spot-detail',
    templateUrl: './spot-detail.component.html'
})
export class SpotDetailComponent implements OnInit {
    spot: ISpot;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ spot }) => {
            this.spot = spot;
        });
    }

    previousState() {
        window.history.back();
    }
}
