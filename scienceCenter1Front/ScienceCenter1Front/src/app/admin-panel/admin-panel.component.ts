import { Component, OnInit } from '@angular/core';
import { Route } from '@angular/compiler/src/core';
import { Router } from '@angular/router';
import { AuthentificationService } from '../services/authentification.service';

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.css']
})
export class AdminPanelComponent implements OnInit {

  private users=[];

  constructor(private authentificationService: AuthentificationService, private router: Router) {

    //Get all fields for form
    let x = authentificationService.getAllUsers();
    x.subscribe(
      res => {
        this.users=res;
      },
      err => {
        alert(err.error);
      }
    );

  }
  ngOnInit() {
  }

}
