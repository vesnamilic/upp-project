import { Component, OnInit, Input } from '@angular/core';
import { ShoppingItem } from '../model/shopping-cart';
import { ShoppingCartService } from '../services/shopping-cart.service';
import { UserOrder } from '../model/user-order';
import { UserOrderService } from '../services/user-order.service';

@Component({
  selector: 'app-magazine',
  templateUrl: './magazine.component.html',
  styleUrls: ['./magazine.component.css']
})
export class MagazineComponent implements OnInit {

  _magazine: any;

  get magazine(): any {
    return this._magazine;
  }

  @Input('magazine')
  set magazine(value: any) {
    this._magazine = value;
  }


  constructor(private shoppingCartService: ShoppingCartService, private userOrderService: UserOrderService) { }

  ngOnInit() {
  }

  addToShoppingCart() {
    const item = new ShoppingItem(this.magazine.id, this.magazine.name, this.magazine.price, 'magazine', this.magazine.id);
    const success = this.shoppingCartService.addToShoppingCart(item);

    if (success) {
      alert('Successfuly added to your shopping cart!');
    } else {
      alert('This item already exists in your shopping cart!');
    }

  }

  subscribe() {
    this.userOrderService.subscribeToMagazine(this.magazine.email).subscribe(
      data => {
        document.location.href = data.url;
      },
      errors => {
        console.log(errors);
      }
    );
  }

}
