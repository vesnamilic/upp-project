import { Component, OnInit } from '@angular/core';
import { TaskService } from '../services/task.service';
import { Router } from '@angular/router';
import { UsersService } from '../services/users.service';
@Component({
  selector: 'app-administrator-tasks',
  templateUrl: './administrator-tasks.component.html',
  styleUrls: ['./administrator-tasks.component.css']
})
export class AdministratorTasksComponent implements OnInit {

  groups = [];
  users = [];
  showMembers = false;
  members = [];

  constructor(private router: Router, private userService: UsersService) { }

  ngOnInit() {
    this.getGroups();
    this.getUsers();
  }

  getGroups() {
    this.userService.getGroups().subscribe(
      data => {
        this.groups = data;
        console.log(data);
      },
      erros => {
        console.log(erros);
      }
    );
  }

  getUsers() {
    this.userService.getUsers().subscribe(
      data => {
        this.users = data;
        console.log(data);
      },
      erros => {
        console.log(erros);
      }
    );
  }

  seeGroupMemebers(groupId: string) {
    this.userService.getGroupMembers(groupId).subscribe(
      data => {
        this.showMembers = true;
        this.members = data;
        console.log(data);
      },
      erros => {
        console.log(erros);
      }
    );
  }

}
