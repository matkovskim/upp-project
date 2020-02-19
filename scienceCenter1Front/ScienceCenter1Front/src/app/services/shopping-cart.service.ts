import { Injectable } from '@angular/core';

const SHOPPING_ITEMS = 'items';
const SHOPPING_ITEMS_TYPE = 'items_type';

@Injectable({
  providedIn: 'root'
})
export class ShoppingCartService {

  shoppingItems: ShoppingItem[];

  constructor() { }


  public getShoppingCart(): string {
    if (window.sessionStorage.getItem(SHOPPING_ITEMS) != null) {
      console.log(sessionStorage.getItem(SHOPPING_ITEMS));
      return sessionStorage.getItem(SHOPPING_ITEMS);
    }
    return null;
  }

  public getShoppingItemsList(): ShoppingItem[] {
    if (window.sessionStorage.getItem(SHOPPING_ITEMS) != null) {
      return JSON.parse(window.sessionStorage.getItem(SHOPPING_ITEMS));
    }
    return [];
  }

  public addToShoppingCart(item: ShoppingItem): boolean {
    const shoppingCart = this.getShoppingCart();
    if (shoppingCart == null) {
      this.shoppingItems = [];
      this.saveType(item.type);
    } else {
        if (item.type !== this.getType()) {
          this.shoppingItems = [];
        this.saveType(item.type);
        } else {
            if(item.type==='magazine'){
              this.shoppingItems = [];
            }
            else{
              if(this.magazineIdOfFirt()!==item.magazineId){
                this.shoppingItems = [];
              }
              else{
                this.shoppingItems = JSON.parse(shoppingCart);
                console.log(this.shoppingItems);
              }
            }
        }
    }

    const oldItem = this.shoppingItems.filter(i => i.id === item.id );

    if (oldItem.length > 0 ) {
      return false;
    }

    this.shoppingItems.push(item);
    this.saveCart();
    return true;
  }

  magazineIdOfFirt(){
    let shoppingCart=this.getShoppingCart();
    if(shoppingCart!==null){
      this.shoppingItems = JSON.parse(shoppingCart);
      return this.shoppingItems[0].magazineId;
    }
    return null;
  }

  saveType(type: string) {
    window.sessionStorage.removeItem(SHOPPING_ITEMS_TYPE);
    window.sessionStorage.setItem(SHOPPING_ITEMS_TYPE, type);
  }

  getType(): string {
    return window.sessionStorage.getItem(SHOPPING_ITEMS_TYPE);
  }

  saveCart() {
    window.sessionStorage.removeItem(SHOPPING_ITEMS);
    window.sessionStorage.setItem(SHOPPING_ITEMS, JSON.stringify(this.shoppingItems));
  }

  public removeFromShoppingCart(id: number): boolean {
    const shoppingCart = this.getShoppingCart();
    if (shoppingCart == null) {
      this.shoppingItems = [];
      return false;
    } else {
      this.shoppingItems = JSON.parse(shoppingCart);
    }
    // TODO dodaj proveru da li postoji u listi taj
    this.shoppingItems = this.shoppingItems.filter(item => item.id !== id );

    if (this.shoppingItems.length === 0) {
      this.emptyShoppingCart();
    } else {
      this.saveCart();
    }
    return true;
  }

  public emptyShoppingCart() {
    window.sessionStorage.removeItem(SHOPPING_ITEMS_TYPE);
    window.sessionStorage.removeItem(SHOPPING_ITEMS);
  }

}

export class ShoppingItem {
  constructor(public id: number, public name: string, public price: number, public type: string, public magazineId: string) { }
}