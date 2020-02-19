import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from '../authentication/token-storage.service';
import { MagazineService } from '../services/magazineService';
import { PaymentService } from '../services/payment.service';
import { ShoppingCartService } from '../services/shopping-cart.service';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements OnInit {

  private loggedRegUser=false;
  private notLoggedRegUser=false;
  private magazines = [];

  constructor(private tokenStorage: TokenStorageService, private magazineService:MagazineService, private paymentService:PaymentService, private shoppingCartService:ShoppingCartService) {
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

  
  addToCart(magazine: any) {
    console.log(magazine);
    let shoppingItem=new ShoppingItem(magazine.id, magazine.name, magazine.subscriptionPrice, "magazine");
    this.shoppingCartService.addToShoppingCart(shoppingItem);
  }

  subscription(magazine: any) {
    let subscriptionDTO=new SubscriptionDTO(magazine.email);
    let x = this.paymentService.subscriptionForMagazines(subscriptionDTO);
    x.subscribe(
      res => {
        document.location.href  = res.url;
      },
      err => {
        console.log(err);
      }
    ); 
   }

}

export class SubscriptionDTO {
  constructor(private email: string) { }
}

export class ShoppingItem {
  constructor(public id: number, public name: string, public price: number, public type: string) { }
}