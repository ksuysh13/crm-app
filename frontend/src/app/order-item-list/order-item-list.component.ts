import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { OrderItemService } from '../data/services/order-item.service';
import { OrderItem } from '../data/models/OrderItem';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';
import { MatCardModule } from '@angular/material/card';
import { OrderService } from '../data/services/order.service';
import { Order } from '../data/models/Order';
import { ProductService } from '../data/services/product.service';
import { DiscountService } from '../data/services/discount.service';
import { AuthService } from '../data/services/auth.service';

@Component({
  selector: 'app-order-item-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule
  ],
  templateUrl: './order-item-list.component.html',
  styleUrls: ['./order-item-list.component.css']
})
export class OrderItemListComponent implements OnInit {
  orderItems: OrderItem[] = [];
  displayedColumns: string[] = ['product', 'quantity', 'price', 'discount', 'total'];
  clientId!: number;
  orderId!: number;
  order!: Order | null;
  isOrderCompleted: boolean = false;

  authService = inject(AuthService);

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private orderItemService: OrderItemService,
    private orderService: OrderService,
    private productService: ProductService,
    private discountService: DiscountService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.clientId = +params['clientId'];
      this.orderId = +params['orderId'];
      
      if (isNaN(this.clientId) || isNaN(this.orderId)) {
        console.error('Invalid clientId or orderId');
        this.router.navigate(['/']);
        return;
      }

      if (this.authService.isAuth() && this.authService.role === 'ADMIN') {
        this.displayedColumns.push('actions');
      }

      this.loadOrder();
      this.loadOrderItems();
    });
  }

  loadOrder(): void {
    this.orderService.getOrderById(this.clientId, this.orderId).subscribe({
      next: (order) => {
        this.order = order;
        this.isOrderCompleted = order.isCompleted;
      },
      error: (err) => console.error('Error loading order', err)
    });
  }

  loadOrderItems(): void {
    this.orderItemService.getOrderItemsByOrder(this.orderId).subscribe({
        next: (items: any[]) => {
            this.orderItems = items.map(item => ({
                ...item,
                product: {
                    productId: item.productId,
                    productName: item.productName,
                    price: item.price
                },
                discount: item.discountId ? {
                    discountId: item.discountId,
                    discountPercentage: parseFloat(item.discountInfo?.replace('%', '')) || 0
                } : undefined
            }));
            console.log('Processed order items:', this.orderItems);
        },
        error: (err) => console.error('Error loading order items', err)
    });
}

  onAddItem(): void {
    this.router.navigate(['/clients', this.clientId, 'orders', this.orderId, 'order-items', 'new']);
  }

  onEditItem(orderItemId: number): void {
    this.router.navigate(['/clients', this.clientId, 'orders', this.orderId, 'order-items', orderItemId]);
  }

  onDeleteItem(orderItemId: number): void {
    if (confirm('Вы уверены, что хотите удалить этот элемент заказа?')) {
      this.orderItemService.deleteOrderItem(orderItemId).subscribe({
        next: () => {
          this.loadOrderItems();
          this.loadOrder(); 
        },
        error: (err) => console.error('Error deleting order item', err)
      });
    }
  }

  calculateItemTotal(item: OrderItem): number {
    const price = item.price;
    const quantity = item.quantity;
    const discount = item.discount?.discountPercentage || 0;
    
    return price * quantity;
  }

  getOrderTotal(): number {
    return this.orderItems.reduce((total, item) => total + this.calculateItemTotal(item), 0);
  }

  backToOrders(): void {
    this.router.navigate(['/clients', this.clientId, 'orders']);
  }
}