import { Component, OnInit } from '@angular/core';
import { TaskService } from '../services/task.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-administrator-tasks',
  templateUrl: './administrator-tasks.component.html',
  styleUrls: ['./administrator-tasks.component.css']
})
export class AdministratorTasksComponent implements OnInit {

  tasks = [];

  constructor(private router: Router, private taskService: TaskService) { }

  ngOnInit() {
    this.taskService.getAllTasks().subscribe(
      data => {
        this.tasks = data;
        console.log(data);
      },
      errors => {
        console.log(errors);
        if (errors.status === 401) {
          this.router.navigateByUrl('/unauthorized');
        }
      }
    );
  }

  getTask(taskId: String) {
    this.router.navigateByUrl('/tasks/' + taskId);
  }

}
