import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MagazineService } from '../services/magazineService';
import { ShoppingCartService } from '../services/shopping-cart.service';

@Component({
  selector: 'app-publications',
  templateUrl: './publications.component.html',
  styleUrls: ['./publications.component.css']
})
export class PublicationsComponent implements OnInit {

  private magazineId:any;
  private publications = [];

  constructor(private activatedRoute: ActivatedRoute, private magazineService:MagazineService, private shoppingCartService:ShoppingCartService) { }

  ngOnInit() {
    
    this.activatedRoute.paramMap.subscribe(
      params => {
        this.magazineId = params.get('magazineId');
      });
      let x = this.magazineService.getAllMagazinesPublications(this.magazineId);

      x.subscribe(
        res => {
          this.publications=res;
          console.log(res);
        },
        err => {
          console.log("Error occured");
        }
      );

  }

  addToCart(publication: any) {
    console.log(publication);
    let shoppingItem=new ShoppingItem(publication.id, publication.number, publication.magazine.publicationPrice, "publication", publication.magazine.id);
    this.shoppingCartService.addToShoppingCart(shoppingItem);
  }

}

export class ShoppingItem {
  constructor(public id: number, public name: string, public price: number, public type: string, public magazineId: string) { }
}