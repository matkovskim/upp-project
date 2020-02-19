import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MagazineService } from '../services/magazineService';
import { ShoppingCartService } from '../services/shopping-cart.service';

@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.css']
})
export class ArticleComponent implements OnInit {

  private publicationId:any;
  private articles = [];

  constructor(private activatedRoute: ActivatedRoute, private magazineService:MagazineService, private shoppingCartService:ShoppingCartService) { }

  ngOnInit() {

    this.activatedRoute.paramMap.subscribe(
      params => {
        this.publicationId = params.get('publicationId');
      });
      let x = this.magazineService.getAllPublicationArticles(this.publicationId);
  
      x.subscribe(
        res => {
          this.articles=res;
          console.log(res);
        },
        err => {
          console.log("Error occured");
        }
      );

  }

  addToCart(article: any) {
    console.log(article);
    let shoppingItem=new ShoppingItem(article.id, article.title, article.publication.magazine.articlePrice, "article");
    this.shoppingCartService.addToShoppingCart(shoppingItem);
  }

}

export class ShoppingItem {
  constructor(public id: number, public name: string, public price: number, public type: string) { }
}