import { inject, Injectable } from '@angular/core';
import { environment } from '../../enviroment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Discount } from '../models/Discount';

@Injectable({
  providedIn: 'root'
})
export class DiscountService {
  private apiUrl = `${environment.apiUrl}discounts`;

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor() { }

  http: HttpClient = inject(HttpClient);

  getAllDiscounts(): Observable<Discount[]> {
    return this.http.get<Discount[]>(`${this.apiUrl}`)
  }

  getDiscountById(id: number): Observable<Discount> {
    return this.http.get<Discount>(`${this.apiUrl}/${id}`);
  }

  createDiscount(discount: Discount): Observable<Discount> {
    return this.http.post<Discount>(this.apiUrl, discount);
  }
  
  updateDiscount(id: number, discount: Discount): Observable<Discount> {
    return this.http.put<Discount>(`${this.apiUrl}/${id}`, discount);
  }

  deleteDiscount(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
