import { inject, Injectable } from '@angular/core';
import { environment } from '../../enviroment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProductGroup } from '../models/ProductGroup'; 

@Injectable({
  providedIn: 'root'
})
export class ProductGroupService {
  private apiUrl = `${environment.apiUrl}productGroup`;

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor() {}

  http: HttpClient = inject(HttpClient);

  getAllProductGroups(): Observable<ProductGroup[]> {
    return this.http.get<ProductGroup[]>(`${this.apiUrl}`);
  }

  getProductGroupById(id: number): Observable<ProductGroup> {
    return this.http.get<ProductGroup>(`${this.apiUrl}/${id}`);
  }

  createProductGroup(productGroup: ProductGroup): Observable<ProductGroup> {
    return this.http.post<ProductGroup>(this.apiUrl, productGroup);
  }

  updateProductGroup(id: number, productGroup: ProductGroup): Observable<ProductGroup> {
    return this.http.put<ProductGroup>(`${this.apiUrl}/${id}`, productGroup);
  }

  deleteProductGroup(groupId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${groupId}`);
  }
}