import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from '../authentication/token-storage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {

  private notLogged: any;
  private logged: any;
  private loggedAdmin:any
  constructor(private tokenStorage: TokenStorageService, private router: Router) {

  }

  ngOnInit() {
    if (this.tokenStorage.getToken() != null) {
      this.notLogged = false;
      this.logged = true;
      console.log(this.tokenStorage.getAuthority());
      if(this.tokenStorage.getAuthority()=="ROLE_ADMIN"){
        this.loggedAdmin=true;
      }
      else{
        this.loggedAdmin=false;
      }
    }
    else {
      this.notLogged = true;
      this.logged = false;
      this.loggedAdmin=false;
    }
  }

  logOut() {
    this.tokenStorage.signOut();
    this.router.navigate(['']);
    this.notLogged = true;
    this.logged = false;
  }

}
