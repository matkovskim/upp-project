import { Component, OnInit } from '@angular/core';
import { RepositoryService } from '../services/repositoryService';
import { TokenStorageService } from '../authentication/token-storage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private repositoryService: RepositoryService, private tokenStorage: TokenStorageService, private router: Router) {}

  ngOnInit() {
  }

  onSubmit(value) {

    let o = new Array();
    for (var property in value) {
      o.push({ fieldId: property, fieldValue: value[property] });
    }

    let x = this.repositoryService.loginUser(o);

    x.subscribe(
      res => {
          this.tokenStorage.saveDate(res.expiratonDate);
          this.tokenStorage.saveToken(res.token);
          this.tokenStorage.saveUsername(res.email);
          this.tokenStorage.saveAuthorities(res.authorities);
          window.location.href = '';
        },
      err => {
        alert(err.error);
      }
    );
  }

}