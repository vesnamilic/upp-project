import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TaskService } from '../services/task.service';
import { NgForm } from '@angular/forms';
import { IDropdownSettings } from 'ng-multiselect-dropdown';
import { ScientificPaperService } from '../services/scientific-paper.service';


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
  selectedFiles: FileList;
  currentFileUpload: File;
  title: String;

  constructor(private router: Router, private taskService: TaskService, private activatedRoute: ActivatedRoute,
    private scientificPaperService: ScientificPaperService) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(params => {
      this.taskId = params.get('id');
    });

    this.dropdownSettings = {
      singleSelection: false,
      idField: 'item_id',
      textField: 'item_text',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 3,
      allowSearchFilter: true
    };

    this.getTask(this.taskId);
  }

  getTask(taskId: String) {
    this.taskService.getTask(taskId).subscribe(
      data => {
        if (data != null) {
          this.title = data.taskName;
          this.initFields(data);
        } else {
          this.processFinished = true;
        }
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
    const submitedData = [];
    const values = Object.entries(form.value);
    console.log(form.value);

    for (const property of values) {
      if (property[0] !== 'pdf') {
        submitedData.push({ fieldId: property[0], fieldValue: property[1] });
      } else if (property[1] != null) {
        this.currentFileUpload = this.selectedFiles.item(0);
      }

    }

    this.taskService.commitTaskForm(submitedData, this.formFieldsDto.taskId).subscribe(
      data => {
        console.log(this.currentFileUpload);
        console.log(data);
        if (this.currentFileUpload !== undefined) {
          this.scientificPaperService.pushFileToStorage(this.currentFileUpload, this.processInstance).subscribe(
            data2 => {
              this.currentFileUpload = undefined;
            },
            error2 => {
              console.log(error2);
            });
        }
        if (data != null) {
          if (data['url'] != null) {
            document.location.href = data.url;
          } else {
            this.title = data.taskName;
            this.initFields(data);
          }

        } else {
          this.processFinished = true;
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

  selectFile(event) {
    console.log(event);
    this.selectedFiles = event.target.files;
  }

}
