import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TaskService } from '../services/task.service';
import { NgForm } from '@angular/forms';
import { IDropdownSettings } from 'ng-multiselect-dropdown';

@Component({
  selector: 'app-user-confirmation-form',
  templateUrl: './user-confirmation-form.component.html',
  styleUrls: ['./user-confirmation-form.component.css']
})
export class UserConfirmationFormComponent implements OnInit {

  formFieldsDto = null;
  formFields = [];
  processInstance = '';
  selectedItems = [];
  dropdownList = [];
  enumLabels = [];
  enumNames = [];
  enumSingleSelectValues = {};
  dropdownSettings: IDropdownSettings = {};
  taskId: String;
  processFinished: Boolean = false;

  constructor(private router: Router, private taskService: TaskService, private activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(params => {
      this.taskId = params.get('id');
    });

    this.getTask(this.taskId);
  }

  getTask(taskId: String) {
    this.taskService.getTask(taskId).subscribe(
      data => {
        console.log(data);
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
      },
      errors => {
        console.log(errors);
        if (errors.status === 401) {
          this.router.navigateByUrl('/unauthorized');
        }
      }
    );
  }

  onSubmit(form: NgForm) {
    const submitedData = new Array();
    const values = Object.entries(form.value);
    console.log(form.value);

    for (const property of values) {
        submitedData.push({ fieldId: property[0], fieldValue: property[1] });
    }

    this.taskService.commitTaskForm(submitedData, this.formFieldsDto.taskId).subscribe(
      data => {
        this.processFinished  = true;
      },
      errors => {
        console.log(errors);
        if (errors.status === 401) {
          this.router.navigateByUrl('/unauthorized');
        }
      }
    );
  }

}
