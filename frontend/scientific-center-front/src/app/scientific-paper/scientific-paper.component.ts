import { Component, OnInit, Input } from '@angular/core';
import { ShoppingItem } from '../model/shopping-cart';
import { ShoppingCartService } from '../services/shopping-cart.service';

@Component({
  selector: 'app-scientific-paper',
  templateUrl: './scientific-paper.component.html',
  styleUrls: ['./scientific-paper.component.css']
})
export class ScientificPaperComponent implements OnInit {

  _paper: any;
  _paymentPossible: boolean;
  _paperPrice: number;
  _magazineId: number;

  get paper(): any {
    return this._paper;
  }

  @Input('paper')
  set paper(value: any) {
    this._paper = value;
  }

  get paymentPossible(): any {
    return this._paymentPossible;
  }

  @Input('paymentPossible')
  set paymentPossible(value: any) {
    this._paymentPossible = value;
  }

  get paperPrice(): any {
    return this._paperPrice;
  }

  @Input('paperPrice')
  set paperPrice(value: any) {
    this._paperPrice = value;
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
    const item = new ShoppingItem(this.paper.id, this.paper.title, this.paperPrice, 'scientificPaper', this.magazineId);
    const success = this.shoppingCartService.addToShoppingCart(item);

    if (success) {
      alert('Successfuly added to your shopping cart!');
    } else {
      alert('This item already exists in your shopping cart!');
    }

  }

}
