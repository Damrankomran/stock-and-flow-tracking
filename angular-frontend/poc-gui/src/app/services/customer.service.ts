import { Injectable, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  constructor(
    
    @Inject('customerApi') private apiUrl,
    
    private http: HttpClient
  ) { }

  getAllCustomer(){
    return this.http.get(this.apiUrl);
  }

  getCustomerByID(id){
    return this.http.get(this.apiUrl+"/"+id);
  }

  createCustomer(obj){
    return this.http.post(this.apiUrl+"/", obj);
  }

  updateCustomerByID(id,obj){
    return this.http.put(this.apiUrl+"/"+id,obj);
  }

  deleteCustomerByID(id){
    return this.http.delete(this.apiUrl+"/"+id);
  }

  deleteAllCustomer(){
    return this.http.delete(this.apiUrl);
  }

}
