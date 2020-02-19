import { Component, OnInit } from '@angular/core';
import { ShoppingCartService } from '../services/shopping-cart.service';
import { UserOrderService } from '../services/user-order.service';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.css']
})
export class ShoppingCartComponent implements OnInit {

  items: ShoppingItem[];

  constructor(private shoppingCart: ShoppingCartService, private orderService:UserOrderService) { }

  ngOnInit() {
    this.items = this.shoppingCart.getShoppingItemsList();
    console.log(this.items);
  }

  removeItem(id: number) {
    const success = this.shoppingCart.removeFromShoppingCart(id);

    if (success) {
      this.items = this.items.filter(item => item.id !== id );
    }
  }

  sendOrder() {
    if (this.items.length === 0) {
      alert('Your shopping cart is empty');
    }
    const ids: number[] = [];
    for (const item of this.items) {
      ids.push(item.id);
    }

    let order = new UserOrder(this.shoppingCart.getType(), ids);
    this.orderService.sendOrder(order).subscribe(
      data => {
        this.shoppingCart.emptyShoppingCart();
        this.items = [];
        document.location.href = data.url;
      },
      errors => {
        console.log(errors);
      }
    );
  }

}

export class ShoppingItem {
  constructor(public id: number, public name: string, public price: number, public type: string) { }
}

export class UserOrder {
  constructor(public type: string, public ids: number[]) {}
}
