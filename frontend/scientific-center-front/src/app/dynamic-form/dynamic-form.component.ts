import { Component, OnInit, Input } from '@angular/core';
import { IDropdownSettings } from 'ng-multiselect-dropdown';
import { TaskService } from '../services/task.service';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-dynamic-form',
  templateUrl: './dynamic-form.component.html',
  styleUrls: ['./dynamic-form.component.css']
})
export class DynamicFormComponent implements OnInit {

  _formFieldsDto = null;
  _formFields = [];
  _processInstance = '';
  formSubmited = false;
  success = false;

  get formFieldsDto(): any {
    return this._formFieldsDto;
  }

  @Input('formFieldsDto')
  set formFieldsDto(value: any) {
    this._formFieldsDto = value;
  }

  get formFields(): any {
    return this._formFields;
  }

  @Input('formFields')
  set formFields(value: any) {
    this._formFields = value;
  }

  get processInstance(): any {
    return this._processInstance;
  }

  @Input('processInstance')
  set processInstance(value: any) {
    this._processInstance = value;
  }

  selectedItems = [];
  dropdownList = [];
  enumLabels = [];
  enumNames = [];
  enumSingleSelectValues = {};
  dropdownSettings: IDropdownSettings = {};

  constructor(private taskService: TaskService) {
    console.log(this.formFields);
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

    this.fieldsFormat();

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
        }
      },
      error => {
        console.log(error);
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
    this.fieldsFormat();
  }


  fieldsFormat() {
    console.log(this.formFields);
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
