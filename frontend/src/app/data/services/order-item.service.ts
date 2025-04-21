import { inject, Injectable } from '@angular/core';
import { environment } from '../../enviroment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OrderItem } from '../models/OrderItem';
import { Product } from '../models/Product';
import { Discount } from '../models/Discount';

@Injectable({
  providedIn: 'root'
})
export class OrderItemService {
  private apiUrl = `${environment.apiUrl}order-items`;

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor() { }

  http: HttpClient = inject(HttpClient);

  getOrderItemsByOrder(orderId: number): Observable<OrderItem[]> {
    return this.http.get<OrderItem[]>(`${this.apiUrl}/order/${orderId}`);
  }

  getOrderItemById(orderItemId: number): Observable<OrderItem> {
    return this.http.get<OrderItem>(`${this.apiUrl}/${orderItemId}`);
  }

  createOrderItem(orderItem: OrderItem): Observable<OrderItem> {
    return this.http.post<OrderItem>(this.apiUrl, orderItem, this.httpOptions);
  }

  updateOrderItem(orderItemId: number, orderItem: OrderItem): Observable<OrderItem> {
    return this.http.put<OrderItem>(`${this.apiUrl}/${orderItemId}`, orderItem, this.httpOptions);
  }

  deleteOrderItem(orderItemId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${orderItemId}`);
  }

  getAllProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${environment.apiUrl}products`);
  }
  
  getAllDiscounts(): Observable<Discount[]> {
    return this.http.get<Discount[]>(`${environment.apiUrl}discounts`);
  }
}