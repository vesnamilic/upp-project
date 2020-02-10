import { Component, OnInit } from '@angular/core';
import { ScientificAreasServiceService } from '../services/scientificAreasService.service';
import { NgForm } from '@angular/forms';
import { Registration } from '../authorization/registrationRequest';
import { RegistrationService } from '../services/registration.service';
import { IDropdownSettings } from 'ng-multiselect-dropdown';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css']
})
export class UserFormComponent implements OnInit {


  scientificAreas = [];
  selectedItems = [];
  dropdownList = [];
  dropdownSettings: IDropdownSettings = {};

  constructor(private scientificService: ScientificAreasServiceService, private registrationService: RegistrationService) { }

  ngOnInit() {
    this.dropdownSettings = {
      singleSelection: false,
      idField: 'id',
      textField: 'name',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 3,
      allowSearchFilter: true
    };
    this.scientificService.getScientificAreas().subscribe(
      data => {
        this.scientificAreas = data;
      },
      errors => {
        console.log(errors);
      }
    );
  }

  onSubmit(form: NgForm) {
    console.log(form);
    console.log(this.selectedItems);
    const array = [];
    for (const item of this.selectedItems) {
      array.push(item.id);
    }

    const registration: Registration = new Registration(form.value['username'], form.value['password'], form.value['firstName'],
    form.value['lastName'], form.value['city'], form.value['country'], form.value['title'], form.value['email'],
    form.value['requestedReviewerRole'], array);

    this.registrationService.registerEditor(registration).subscribe(
      data => {
        console.log('jupi');
      },
      errors => {
        console.log(errors);
      }
    );

  }

}
