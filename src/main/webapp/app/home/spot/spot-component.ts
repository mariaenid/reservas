import { Component, Input, EventEmitter, Output } from '@angular/core';

@Component({
    selector: 'spot-component',
    templateUrl: './spot-component.html',
    styleUrls: ['./spot-component.css']
})
export class SpotComponent {
    @Input() public spot: any;
    @Output() public onSpotClick: EventEmitter<any> = new EventEmitter();

    constructor() {}

    public saveReserve() {
        this.onSpotClick.emit(this.spot);
    }
}
