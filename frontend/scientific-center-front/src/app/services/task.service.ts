import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  taskUrl = 'http://localhost:8080/tasks/';

  constructor(private httpClient: HttpClient) { }

  startProcess(processName: String) {
    return this.httpClient.get<any>(this.taskUrl + 'startProcess?processName=' + processName);
  }

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
