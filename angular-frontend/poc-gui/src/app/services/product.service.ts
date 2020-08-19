import { Injectable, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(
    @Inject('productApi') private apiUrl,

    private http: HttpClient
  ) { }

  getAllProduct(){
    return this.http.get(this.apiUrl);
  }

  getProductByID(id){
    return this.http.get(this.apiUrl+"/"+id);
  }

  createProduct(obj){
    return this.http.post(this.apiUrl, obj);
  }

  updateProductByID(id,obj){
    return this.http.put(this.apiUrl+"/"+id,obj);
  }

  deleteProductByID(id){
    return this.http.delete(this.apiUrl+"/"+id);
  }

  deleteAllProduct(){
    return this.http.delete(this.apiUrl);
  }

}
