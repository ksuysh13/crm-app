// product.service.ts
import { inject, Injectable } from '@angular/core';
import { environment } from '../../enviroment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../models/Product';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = `${environment.apiUrl}products`;

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor() {}

  http: HttpClient = inject(HttpClient);

  getAllProducts(): Observable<Product[]> {
        return this.http.get<Product[]>(`${this.apiUrl}`)
  }

  getProductById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${id}`);
  }
  
  getProductsByGroupId(groupId: number): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/group/${groupId}`);
  }
  
  createProduct(product: Product): Observable<Product> {
    return this.http.post<Product>(
      `${this.apiUrl}/groupId/${product.groupId}/manufacturerId/${product.manufacturerId}`,
      product
    );
  }
  
  updateProduct(product: Product): Observable<Product> {
    return this.http.put<Product>(
      `${this.apiUrl}/groupId/${product.groupId}/manufacturerId/${product.manufacturerId}/productId/${product.productId}`,
      product
    );
  }
  
  deleteProduct(productId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${productId}`);
  }
}