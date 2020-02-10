import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-magazine',
  templateUrl: './magazine.component.html',
  styleUrls: ['./magazine.component.css']
})
export class MagazineComponent implements OnInit {

  _magazine: any;

  get magazine(): any {
    return this._magazine;
  }

  @Input('magazine')
  set magazine(value: any) {
    this._magazine = value;
  }

  constructor() { }

  ngOnInit() {
  }

}
