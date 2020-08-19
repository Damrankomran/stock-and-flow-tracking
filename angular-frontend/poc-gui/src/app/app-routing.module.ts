import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import {HomeComponent} from "./components/home/home.component";
import {CustomerComponent} from "./components/customer/customer.component";
import {ProductComponent} from "./components/product/product.component";
import {OrderManagementComponent} from "./components/order-management/order-management.component";
import {NotFoundComponent} from "./components/not-found/not-found.component";

const routes: Routes = [
  {
    path: "", component: HomeComponent
  },
  {
    path: "customer", component: CustomerComponent
  },
  {
    path: "product", component: ProductComponent
  },
  {
    path: "order", component: OrderManagementComponent
  },
  {
    path: "**", component: NotFoundComponent
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
