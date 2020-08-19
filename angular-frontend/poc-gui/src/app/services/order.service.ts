import { Injectable, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  constructor(
    @Inject('orderApi') private apiUrl,

    private http: HttpClient
  ) { }

  getAllOrders(){
    return this.http.get(this.apiUrl);
  }

  getOrderByID(id){
    return this.http.get(this.apiUrl+"/"+id);
  }

  createOrder(obj){
    return this.http.post(this.apiUrl, obj);
  }

  updateOrderByID(id,obj){
    return this.http.put(this.apiUrl+"/"+id,obj);
  }

  deleteOrderByID(id){
    return this.http.delete(this.apiUrl+"/"+id);
  }

}
