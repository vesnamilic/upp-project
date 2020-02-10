import { Component, OnInit } from '@angular/core';
import { TaskService } from '../services/task.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.css']
})
export class TaskListComponent implements OnInit {

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
