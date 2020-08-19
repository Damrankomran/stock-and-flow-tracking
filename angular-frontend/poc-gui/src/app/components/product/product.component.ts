import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { MatSnackBar } from '@angular/material/snack-bar';

import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss']
})
export class ProductComponent implements OnInit {

  displayedColumns: string[] = ['productID', 'name', 'price', 'count', 'updateDate', 'delete'];
  dataSource = new MatTableDataSource<Product>(ProductList);
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  product = {
    productID: '',
    name: '',
    price: '',
    count: '',
    updateDate: ''
  };

  selectedRowItem: number = -1;

  constructor(
    private productService: ProductService,
    private _snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.dataSource.paginator = this.paginator;
    this.getAllProduct();
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
  }

  public highlight(row) {
    this.selectedRowItem = row;
  }

  // ----------- Product Process ----------- //

  public getAllProduct() {
    ProductList = [];
    this.productService.getAllProduct().subscribe((res) => {
      Object.keys(res).forEach((key) => {
        ProductList.push(res[key]);
      });
      this.dataSource.data = ProductList;
    }, (err) => {
      console.log(err);
    });
  }

  public createProduct() {
    this.productService.createProduct(this.product).subscribe((res) => {
      console.log("Create product successful! customer: " + JSON.stringify(this.product));
      this.openSnackBar("Create product successful!");
      this.getAllProduct();
      this.clearForm();
    }, (err) => {
      this.openSnackBar("Create product failed!");
    });
  }

  public updateProductByID = (id) => {
    //productID isn't updated
    this.product.productID = null;
    //updateDate is generatedValue
    this.product.updateDate = null;

    this.productService.updateProductByID(id, this.product).subscribe((res) => {
      console.log("Update product successful! customer: " + JSON.stringify(this.product));
      this.openSnackBar("Update product successful!");
      this.getAllProduct();
      this.clearForm();
    }, (err) => {
      console.log(err);
      this.openSnackBar("Update product failed!");
    });
  }

  public deleteProductByID = (id) => {
    if (confirm('Are you want to delete this product?'))
      this.productService.deleteProductByID(id).subscribe((res) => {
        console.log(id + "Delete product successful!");
        this.openSnackBar("Delete product successful!");
        this.getAllProduct();
        this.clearForm();
      }, (err) => {
        this.openSnackBar("Delete product failed!");
        console.log(err);
      });
  }

  // ----------- Form Process ----------- //

  public doFilter = (value: string) => {
    this.dataSource.filter = value.trim().toLocaleLowerCase();
  }

  public getProduct(element) {
    this.product.productID = element.productID;
    this.product.name = element.name;
    this.product.price = element.price;
    this.product.count = element.count;
    this.product.updateDate = element.updateDate;
  }

  public clearForm() {
    this.product.productID = '';
    this.product.name = '';
    this.product.price = '';
    this.product.count = '';
    this.product.updateDate = '';

    this.selectedRowItem = -1;
  }

  openSnackBar(message: string) {
    this._snackBar.open(message, "OK", {
      duration: 4000,
    });
  }

}

export interface Product {
  productID: number;
  name: string;
  price: number;
  count: string;
  updateDate: string;
}

var ProductList: Product[] = [];

