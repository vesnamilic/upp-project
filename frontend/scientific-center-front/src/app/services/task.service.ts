import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  taskUrl = 'https://localhost:9991/tasks/';

  constructor(private httpClient: HttpClient) { }

  getAllTasks() {
    return this.httpClient.get<any>(this.taskUrl + 'getTasks/');
  }

  getTask(taskId: String) {
    return this.httpClient.get<any>(this.taskUrl + taskId);
  }

  commitTaskForm(dto, taskId) {
    return this.httpClient.post<any>(this.taskUrl + 'commitForm/' + taskId, dto);
  }

}
