import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSort } from '@angular/material/sort';

import { CustomerService } from '../../services/customer.service';
import { ProductService } from '../../services/product.service';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  displayedCustomerListColumns: string[] = ['customerID', 'name', 'phone'];
  displayedProductListColumns: string[] = ['productID', 'name', 'price', 'count'];
  displayedOrderListColumns: string[] = ['orderID', 'customerName', 'productName', 'price', 'count', 'orderDate'];

  customerDataSource = new MatTableDataSource<Customer>(CustomerList);
  productDataSource = new MatTableDataSource<Product>(ProductList);
  orderDataSource = new MatTableDataSource<Order>(OrderList);

  @ViewChild('TableCustomerPaginator', { static: true }) customerPaginator: MatPaginator;
  @ViewChild('TableCustomerSort') customerSort: MatSort;

  @ViewChild('TableProductPaginator', { static: true }) productPaginator: MatPaginator;
  @ViewChild('TableProductSort') productSort: MatSort;

  @ViewChild('TableOrderPaginator', { static: true }) orderPaginator: MatPaginator;
  @ViewChild('TableOrderSort') orderSort: MatSort;

  constructor(
    private customerService: CustomerService,
    private productService: ProductService,
    private orderService: OrderService,
    private _snackBar: MatSnackBar
  ) { }

  order = {
    orderID: '',
    customerID: '',
    productID: '',
    count: 1,
    price: 0
  }

  tempPrice = 0;
  productName = "";
  customerName = "";

  selectedCustomerDataSourceRowIndex: number = -1;
  selectedProductDataSourceRowIndex: number = -1;

  ngOnInit() {

    this.customerDataSource.paginator = this.customerPaginator;
    this.productDataSource.paginator = this.productPaginator;
    this.orderDataSource.paginator = this.orderPaginator;

    this.getAllCustomer();
    this.getAllProduct();
    this.getAllOrders();
  }

  ngAfterViewInit(): void {
    this.customerDataSource.sort = this.customerSort;
    this.productDataSource.sort = this.productSort;
    this.orderDataSource.sort = this.orderSort;
  }

  public highlight(dataSourceType, row) {
    if (dataSourceType == "customer") {
      this.selectedCustomerDataSourceRowIndex = row;
    }
    else if (dataSourceType == "product") {
      this.selectedProductDataSourceRowIndex = row;
    }
  }

  public doFilter = (dataSourceType: string, value: string) => {
    if (dataSourceType == "customer") this.customerDataSource.filter = value.trim().toLocaleLowerCase();
    else if (dataSourceType == "product") this.productDataSource.filter = value.trim().toLocaleLowerCase();
    else if (dataSourceType == "order") this.orderDataSource.filter = value.trim().toLocaleLowerCase();
  }

  public getAllCustomer() {
    CustomerList = [];
    this.customerService.getAllCustomer().subscribe((res) => {
      Object.keys(res).forEach((key) => {
        CustomerList.push(res[key]);
      });
      this.customerDataSource.data = CustomerList;
    }, (err) => {
      console.log(err);
    });
  }

  public getAllProduct() {
    ProductList = [];
    this.productService.getAllProduct().subscribe((res) => {
      Object.keys(res).forEach((key) => {
        ProductList.push(res[key]);
      });
      this.productDataSource.data = ProductList;
    }, (err) => {
      console.log(err);
    });
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
        }

        obj.customerName = (await this.getCustomerNameByID(order[index].customerID)).toString();
        obj.productName = (await this.getProductNameByID(order[index].productID)).toString();

        OrderList.push(obj);
      }
      this.orderDataSource.data = OrderList;
    }, (err) => {
      console.log(err);
    });
  }

  public getCustomer(obj) {
    this.order.customerID = obj.customerID;
    this.customerName = obj.name;
  }

  public getProduct(obj) {
    this.order.productID = obj.productID;
    this.order.price = obj.price;
    this.tempPrice = obj.price;
    this.productName = obj.name
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

  // -------------- Order Process -------------- //
  public async createOrder() {
    if (this.order.customerID != "" && this.order.productID != "") {
      this.orderService.createOrder(this.order).subscribe(async (res) => {
        let isUpdate = await this.updateProduct();
        if (isUpdate) {
          console.log("Create order successful! order: " + JSON.stringify(this.order));
          this.openSnackBar("Create order successful!");
          this.getAllOrders();
          this.getAllProduct();
          this.clearForm();
        }
      }, (err) => {
        this.openSnackBar("Create order failed!");
        console.log(err);
      });
    }
    else {
      alert("Lütfen bilgileri eksiksiz doldurunuz!");
    }
  }

  public updateProduct() {
    return new Promise((resolve) => {
      this.productService.getProductByID(this.order.productID).subscribe((product) => {
        //Siparişteki adet'e göre ürünün adetini product tablosunda güncelliyoruz.
        product['count'] = product['count'] - this.order.count;
        //productID isn't updated
        product['productID'] = null;
        //updateDate is generatedValue
        product['updateDate'] = null;
        this.productService.updateProductByID(this.order.productID, product).subscribe((res) => {
          console.log("Update product successful! product: " + JSON.stringify(res));
          resolve(true);
        })
      }, (err) => {
        console.log(err);
        resolve(false);
      });
    });
  }

// -------------- Form Process -------------- //

  public clearForm() {
    this.order.orderID = '';
    this.order.productID = '';
    this.order.customerID = '';
    this.order.price = 0;
    this.order.count = 1;

    this.productName = "";
    this.customerName = "";

    this.selectedCustomerDataSourceRowIndex = -1;
    this.selectedProductDataSourceRowIndex = -1;
  }

  public calculatePrice() {
    this.order.price = this.order.count * this.order.price;
  }

  openSnackBar(message: string) {
    this._snackBar.open(message, "OK", {
      duration: 4000,
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

export interface Customer {
  customerID: Number;
  name: String;
  age: Number;
  phone: String;
  email: String;
  address: String;
}

var CustomerList: Customer[] = [];

export interface Product {
  productID: number;
  name: string;
  price: number;
  count: string;
  updateDate: string;
}

var ProductList: Product[] = [];