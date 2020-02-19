import { Component, OnInit, Input } from '@angular/core';
import { ShoppingCartService } from '../services/shopping-cart.service';
import { ShoppingItem } from '../model/shopping-cart';

@Component({
  selector: 'app-issue',
  templateUrl: './issue.component.html',
  styleUrls: ['./issue.component.css']
})
export class IssueComponent implements OnInit {
  _issue: any;
  _paymentPossible: boolean;
  _issuePrice: number;
  _magazineId: number;

  get issue(): any {
    return this._issue;
  }

  @Input('issue')
  set issue(value: any) {
    this._issue = value;
  }

  get paymentPossible(): any {
    return this._paymentPossible;
  }

  @Input('paymentPossible')
  set paymentPossible(value: any) {
    this._paymentPossible = value;
  }

  @Input('issuePrice')
  set issuePrice(value: any) {
    this._issuePrice = value;
  }

  get issuePrice(): any {
    return this._issuePrice;
  }

  get magazineId(): any {
    return this._magazineId;
  }

  @Input('magazineId')
  set magazineId(value: any) {
    this._magazineId = value;
  }
  constructor(private shoppingCartService: ShoppingCartService) { }

  ngOnInit() {
  }

  addToShoppingCart() {
    const item = new ShoppingItem(this.issue.id, this.issue.number, this.issuePrice, 'issue', this.magazineId);
    const success = this.shoppingCartService.addToShoppingCart(item);

    if (success) {
      alert('Successfuly added to your shopping cart!');
    } else {
      alert('This item already exists in your shopping cart!');
    }

  }

}
