import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { OrderService } from '../data/services/order.service';
import { Order } from '../data/models/Order';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-order-list',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    DatePipe
  ],
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.css']
})
export class OrderListComponent implements OnInit {
  orders: Order[] = [];
  clientId!: number;
  selectedOrderId: number | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private orderService: OrderService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.clientId = +this.route.snapshot.paramMap.get('clientId')!;
    this.loadOrders();
  }

  loadOrders(): void {
    this.orderService.getOrdersByClientId(this.clientId).subscribe({
      next: (data) => this.orders = data,
      error: (err) => console.error('Error loading orders', err)
    });
  }

  onCreateOrder(): void {
    this.router.navigate(['/clients', this.clientId, 'orders', 'new']);
  }

  toggleMenu(orderId: number | undefined): void {
    if (orderId !== undefined) {
      this.selectedOrderId = this.selectedOrderId === orderId ? null : orderId;
    }
  }

  editOrder(orderId: number | undefined): void {
    if (orderId !== undefined) {
      this.router.navigate(['/clients', this.clientId, 'orders', orderId]);
      this.selectedOrderId = null;
    }
  }

  deleteOrder(orderId: number | undefined): void {
    if (orderId !== undefined) {
      if (confirm('Вы уверены, что хотите удалить этот заказ?')) {
        this.orderService.deleteOrder(this.clientId, orderId).subscribe({
          next: () => {
            this.loadOrders();
            this.selectedOrderId = null;
          },
          error: (err) => console.error('Error deleting order', err)
        });
      }
    }
  }

  recalculateOrder(orderId: number): void {
    this.orderService.recalculateOrderTotal(this.clientId, orderId).subscribe({
      next: () => this.loadOrders(),
      error: (err) => console.error('Error recalculating order', err)
    });
  }

  viewOrderItems(orderId: number | undefined): void {
    if (orderId !== undefined) {
        this.router.navigate(['/clients', this.clientId, 'orders', orderId, 'order-items']);
    }
  }
}