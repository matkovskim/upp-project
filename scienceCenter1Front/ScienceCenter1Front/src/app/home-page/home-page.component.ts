import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from '../authentication/token-storage.service';
import { MagazineService } from '../services/magazineService';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements OnInit {

  private loggedRegUser=false;
  private notLoggedRegUser=false;
  private magazines = [];

  constructor(private tokenStorage: TokenStorageService, private magazineService:MagazineService) {
    let x = this.magazineService.getAllMagazines();

    x.subscribe(
      res => {
        this.magazines = res;
        console.log(res);
      },
      err => {
        console.log("Error occured");
      }
    );

  }

  ngOnInit() {
    if (this.tokenStorage.getToken() != null) {
      if(this.tokenStorage.getAuthority()=="ROLE_REG_USER"){
        this.loggedRegUser=true;
      }
      else{
        this.loggedRegUser=false;
      }
    } 
    else{
      this.loggedRegUser=false;
    }
    this.notLoggedRegUser=!this.loggedRegUser;
  }

}
