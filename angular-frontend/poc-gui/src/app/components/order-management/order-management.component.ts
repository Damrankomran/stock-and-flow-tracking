import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSort } from '@angular/material/sort';

import { CustomerService } from '../../services/customer.service';
import { ProductService } from '../../services/product.service';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-order-management',
  templateUrl: './order-management.component.html',
  styleUrls: ['./order-management.component.scss']
})
export class OrderManagementComponent implements OnInit {

  displayedOrderListColumns: string[] = ['orderID', 'customerName', 'productName', 'price', 'count', 'orderDate', 'delete'];
  orderDataSource = new MatTableDataSource<Order>(OrderList);
  @ViewChild('TableOrderPaginator', { static: true }) orderPaginator: MatPaginator;
  @ViewChild('TableOrderSort') orderSort: MatSort;

  order = {
    orderID: '',
    customerName: '',
    productName: '',
    count: '',
    price: ''
  }

  selectedRowItem: number = -1;

  constructor(
    private customerService: CustomerService,
    private productService: ProductService,
    private orderService: OrderService,
    private _snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.orderDataSource.paginator = this.orderPaginator;
    this.getAllOrders();
  }

  ngAfterViewInit(): void {
    this.orderDataSource.sort = this.orderSort;
  }

  public highlight(row) {
    this.selectedRowItem = row;
  }

  public doFilter = (value: string) => {
    this.orderDataSource.filter = value.trim().toLocaleLowerCase();
  }

  public async getAllOrders() {
    OrderList = [];
    this.orderService.getAllOrders().subscribe(async (order) => {
      for (let index in order) {

        let obj = {
          orderID: order[index].orderID,
          customerName: '',
          productName: '',
          count: order[index].count,
          price: order[index].price,
          orderDate: order[index].orderDate
        };

        obj.customerName = (await this.getCustomerNameByID(order[index].customerID)).toString();
        obj.productName = (await this.getProductNameByID(order[index].productID)).toString();

        OrderList.push(obj);
      }
      this.orderDataSource.data = OrderList;
    }, (err) => {
      console.log(err);
    });
  }

  public getOrder(order) {
    this.order.orderID = order.orderID;
    this.order.customerName = order.customerName;
    this.order.productName = order.productName;
    this.order.count = order.count;
    this.order.price = order.price;
  }

  public deleteOrderByID(id) {
    if (confirm('Do you want to delete this order?')) {
      this.orderService.deleteOrderByID(id).subscribe((res) => {
        this.openSnackBar("Delete order successful!");
        this.getAllOrders();
        this.clearForm();
      }, (err) => {
        console.log(err);
      });
    }
  }

  public getCustomerNameByID(customerID) {
    return new Promise((resolve, reject) => {
      this.customerService.getCustomerByID(customerID).subscribe((customer) => {
        resolve(customer['name']);
      }, (err) => {
        console.log(err);
        reject(err);
      });
    });
  }

  public getProductNameByID(productID) {
    return new Promise((resolve, reject) => {
      this.productService.getProductByID(productID).subscribe((product) => {
        resolve(product['name']);
      }, (err) => {
        console.log(err);
        reject(err);
      });
    });
  }

  public clearForm() {
    this.order.orderID = "";
    this.order.customerName = "";
    this.order.productName = "";
    this.order.count = "";
    this.order.price = "";

    this.selectedRowItem = -1;
  }

  openSnackBar(message: string) {
    this._snackBar.open(message, "OK", {
      duration: 2000,
    });
  }

}

export interface Order {
  orderID: Number;
  customerName: String;
  productName: String;
  price: Number;
  count: Number;
}

var OrderList: Order[] = [];
