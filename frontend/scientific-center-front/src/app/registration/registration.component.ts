import { Component, OnInit } from '@angular/core';
import { RegistrationService } from '../services/registration.service';
import { IDropdownSettings } from 'ng-multiselect-dropdown';
import { NgForm } from '@angular/forms';
import { TaskService } from '../services/task.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  formFieldsDto = null;
  formFields = [];
  processInstance = '';
  selectedItems = [];
  dropdownList = [];
  enumLabels = [];
  enumNames = [];
  enumSingleSelectValues = {};
  dropdownSettings: IDropdownSettings = {};
  formSubmited = false;
  success = false;

  constructor(private registrationService: RegistrationService, private taskService: TaskService) {
    this.taskService.startProcess('registration').subscribe(
      data => {
        console.log(data);
        this.initFields(data);
      },
      errors => {
        console.error(errors);
      }
    );
  }

  ngOnInit() {
    this.success = false;
    this.formSubmited = false;
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

    this.formSubmited = true;
    this.taskService.commitTaskForm(submitedData, this.formFieldsDto.taskId).subscribe(
      data => {
        console.log(data);
        if (data != null) {
          this.initFields(data);
        } else {
          this.formSubmited = false;
          this.success = true;
        }
      },
      error => {
        this.formSubmited = false;
        this.success = false;
        console.log('error');
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


