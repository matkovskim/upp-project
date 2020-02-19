import { Component, OnInit } from '@angular/core';
import { UserOrderService } from '../services/user-order.service';

@Component({
  selector: 'app-purchased-items',
  templateUrl: './purchased-items.component.html',
  styleUrls: ['./purchased-items.component.css']
})
export class PurchasedItemsComponent implements OnInit {

  magazines = [];
  issues = [];
  scientificPapers = [];
  subscriptions = [];

  constructor(private userOrderService: UserOrderService) { }

  ngOnInit() {
    this.userOrderService.getAllPurchasedItems().subscribe(
      data => {
        console.log(data);
        this.magazines = data.magazines;
        this.issues = data.issues;
        this.scientificPapers = data.scientificPapers;
        this.subscriptions = data.subscriptions;
      },
      errors => {
        console.log(errors);
      }
    );
  }

}
