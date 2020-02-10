import { Component, OnInit } from '@angular/core';
import { MagazineService } from '../services/magazine.service';

@Component({
  selector: 'app-all-magazines',
  templateUrl: './all-magazines.component.html',
  styleUrls: ['./all-magazines.component.css']
})
export class AllMagazinesComponent implements OnInit {

  allMagazines = [];
  constructor(private magazineService: MagazineService) { }

  ngOnInit() {
    this.getAllMagazines();
  }

  public getAllMagazines() {
    this.magazineService.getAllMagazines().subscribe(
      data => {
        console.log(data)
        this.allMagazines = data;
      },
      errors => {
        console.log(errors);
      }
    );
  }

}
