import { Component, OnInit } from '@angular/core';
import { IDropdownSettings } from 'ng-multiselect-dropdown';
import { NgForm } from '@angular/forms';
import { MagazineService } from '../services/magazine.service';
import { TaskService } from '../services/task.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-magazine-form',
  templateUrl: './magazine-form.component.html',
  styleUrls: ['./magazine-form.component.css']
})
export class MagazineFormComponent implements OnInit {

  formFieldsDto = null;
  formFields = [];
  processInstance = '';
  selectedItems = [];
  dropdownList = [];
  enumLabels = [];
  enumNames = [];
  enumSingleSelectValues = {};
  dropdownSettings: IDropdownSettings = {};
  processFinished: Boolean = false;

  constructor(private router: Router, private magazineService: MagazineService, private taskService: TaskService) {
    this.magazineService.startProcess('magazine_creation').subscribe(
      data => {
        if (data != null) {
          this.initFields(data);
        } else {
          this.processFinished  = true;
        }
      },
      error => {
        console.log(error);
        if (error.status === 401) {
          this.router.navigateByUrl('/unauthorized');
        }
      }

    );

  }

  ngOnInit() {
    this.dropdownSettings = {
      singleSelection: false,
      idField: 'item_id',
      textField: 'item_text',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 3,
      allowSearchFilter: true
    };
  }

  onSubmit(form: NgForm) {
    const submitedData = [];
    const values = Object.entries(form.value);
    console.log(form.value);

    for (const property of values) {
      submitedData.push({ fieldId: property[0], fieldValue: property[1] });
    }

    this.taskService.commitTaskForm(submitedData, this.formFieldsDto.taskId).subscribe(
      data => {
        console.log(data);
        if (data != null) {
          this.initFields(data);
        } else {
          this.processFinished  = true;
        }
      },
      error => {
        console.log(error);
        if (error.status === 401) {
          this.router.navigateByUrl('/unauthorized');
        }
      }
    );
  }

  initFields(data: any) {
    this.selectedItems = [];
    this.dropdownList = [];
    this.enumLabels = [];
    this.enumNames = [];
    this.enumSingleSelectValues = {};
    this.formFieldsDto = data;
    this.formFields = data.formFields;
    this.processInstance = data.processInstanceId;
    this.formFields.forEach((field) => {
      if (field.type.name === 'enum') {
        if (field.properties['multiselect'] !== undefined) {
          const array = [];
          const enumValues = Object.entries(field.type.values);

          for (const value of enumValues) {
            console.log(value[0]);
            console.log(value[1]);
            array.push({ item_id: value[0], item_text: value[1] });
          }
          this.enumLabels.push(field.label);
          this.enumNames.push(field.id);
          this.dropdownList.push(array);
          this.selectedItems.push([]);
          console.log(this.dropdownList);
        } else {

          const enumValues = Object.entries(field.type.values);
          const array = [];
          for (const value of enumValues) {
            array.push(value[0]);
          }
          this.enumSingleSelectValues[field.id] = array;
        }
      }
    });
  }


  onItemSelect(item: any) {
    console.log(item);
  }
  onSelectAll(items: any) {
    console.log(items);
  }


}
