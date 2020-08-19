import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSort } from '@angular/material/sort';

import { CustomerService } from '../../services/customer.service';

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.scss']
})
export class CustomerComponent implements OnInit, AfterViewInit {

  displayedColumns: string[] = ['customerID', 'name', 'age', 'phone', 'email', 'address', 'delete'];
  dataSource = new MatTableDataSource<Customer>(CustomerList);

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    private customerService: CustomerService,
    private _snackBar: MatSnackBar
  ) { }

  customer = {
    customerID: '',
    name: '',
    age: '',
    phone: '',
    email: '',
    address: '',
  }

  selectedRowItem: number = -1;

  ngOnInit(): void {
    this.dataSource.paginator = this.paginator;
    this.getAllCustomer();
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
  }

  public highlight(row) {
    this.selectedRowItem = row;
  }

  // ----------- Customer Process ----------- //

  public getAllCustomer() {
    CustomerList = [];
    this.customerService.getAllCustomer().subscribe((res) => {
      Object.keys(res).forEach((key) => {
        CustomerList.push(res[key]);
      });
      this.dataSource.data = CustomerList;
    }, (err) => {
      console.log(err);
    });
  }

  public createCustomer() {
    this.customerService.createCustomer(this.customer).subscribe((res) => {
      console.log("Create customer successful! customer: " + JSON.stringify(this.customer));
      this.openSnackBar("Create customer successful!");
      this.getAllCustomer();
      this.clearForm();
    }, (err) => {
      this.openSnackBar("Create customer failed!");
      console.log("err --> " + err);
    });
  }

  public updateCustomerByID = (id) => {
    //customerID is generated value. It cannot be sent within the Request Body
    this.customer.customerID = null;

    this.customerService.updateCustomerByID(id, this.customer).subscribe((res) => {
      console.log("update customer successful! " + JSON.stringify(this.customer));
      this.openSnackBar("Update customer successful!");
      this.getAllCustomer();
      this.clearForm();
    }, (err) => {
      this.openSnackBar("Update customer failed!");
    });

  }

  public deleteCustomerByID(id) {
    if (confirm('Do you want to delete this customer?'))
      this.customerService.deleteCustomerByID(id).subscribe((res) => {
        this.openSnackBar("Delete customer successful!");
        this.getAllCustomer();
        this.clearForm();
      }, (err) => {
        this.openSnackBar("Delete customer failed!");
      });

  }

  // ----------- Form Process ----------- //

  public getCustomer(obj) {
    this.customer.customerID = obj.customerID;
    this.customer.name = obj.name;
    this.customer.age = obj.age;
    this.customer.phone = obj.phone;
    this.customer.email = obj.email;
    this.customer.address = obj.address;
  }

  public doFilter = (value: string) => {
    this.dataSource.filter = value.trim().toLocaleLowerCase();
  }

  public clearForm() {
    this.customer.customerID = "";
    this.customer.name = "";
    this.customer.age = "";
    this.customer.phone = "";
    this.customer.email = "";
    this.customer.address = "";

    this.selectedRowItem = -1;
  }

  openSnackBar(message: string) {
    this._snackBar.open(message, "OK", {
      duration: 4000,
    });
  }

}

export interface Customer {
  customerID: Number;
  name: String;
  age: Number;
  phone: String;
  email: String;
  address: String;
}

var CustomerList: Customer[] = [];

