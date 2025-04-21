import { inject, Injectable } from '@angular/core';
import { environment } from '../../enviroment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Order } from '../models/Order';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiUrl = `${environment.apiUrl}orders`;

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor() {}

  http: HttpClient = inject(HttpClient);

  getOrdersByClientId(clientId: number): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.apiUrl}/${clientId}`);
  }

  getOrderById(clientId: number, orderId: number): Observable<Order> {
    return this.http.get<Order>(`${this.apiUrl}/${clientId}/${orderId}`);
  }

  createOrder(clientId: number, order: Order): Observable<Order> {
    order.clientId = clientId;
    return this.http.post<Order>(`${this.apiUrl}/${clientId}`, order, this.httpOptions);
  }

  updateOrder(clientId: number, orderId: number, order: Order): Observable<Order> {
    return this.http.put<Order>(`${this.apiUrl}/${clientId}/${orderId}`, order, this.httpOptions);
  }

  deleteOrder(clientId: number, orderId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${clientId}/${orderId}`);
  }

  recalculateOrderTotal(clientId: number, orderId: number): Observable<Order> {
    return this.http.put<Order>(`${this.apiUrl}/${clientId}/${orderId}/recalculate`, {}, this.httpOptions);
  }
}