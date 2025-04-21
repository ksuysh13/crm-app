import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { OrderItemService } from '../data/services/order-item.service';
import { OrderItem } from '../data/models/OrderItem';
import { ProductService } from '../data/services/product.service';
import { DiscountService } from '../data/services/discount.service';
import { Product } from '../data/models/Product';
import { Discount } from '../data/models/Discount';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-edit-order-item',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule
  ],
  templateUrl: './edit-order-item.component.html',
  styleUrls: ['./edit-order-item.component.css']
})
export class EditOrderItemComponent implements OnInit {
  orderItemForm: FormGroup;
  isEditMode = false;
  orderItemId?: number;
  clientId!: number;
  orderId!: number;
  products: Product[] = [];
  discounts: Discount[] = [];
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private orderItemService: OrderItemService,
    private productService: ProductService,
    private discountService: DiscountService,
    private snackBar: MatSnackBar
  ) {
    this.orderItemForm = this.fb.group({
      productId: ['', Validators.required],
      quantity: [1, [Validators.required, Validators.min(1)]],
      discountId: [null],
      price: [{value: '', disabled: true}]
    });
  }

  ngOnInit(): void {
    this.clientId = +this.route.snapshot.params['clientId'];
    this.orderId = +this.route.snapshot.params['orderId'];
    this.orderItemId = this.route.snapshot.params['orderItemId'];
    
    // Проверка, является ли orderItemId числом, а не NaN
    this.isEditMode = typeof this.orderItemId === 'number' && !isNaN(this.orderItemId);
    
    this.loadProducts();
    this.loadDiscounts();
    if (this.isEditMode) {
        this.loadOrderItem();
    }
    
    this.orderItemForm.get('productId')?.valueChanges.subscribe(productId => {
        this.updatePrice(productId);
    });
}

  loadProducts(): void {
    this.productService.getAllProducts().subscribe({
      next: (products) => this.products = products,
      error: (err) => console.error('Error loading products', err)
    });
  }

  loadDiscounts(): void {
    this.discountService.getAllDiscounts().subscribe({
      next: (discounts) => this.discounts = discounts,
      error: (err) => console.error('Error loading discounts', err)
    });
  }

  loadOrderItem(): void {
    if (!this.orderItemId) return;

    this.isLoading = true;
    this.orderItemService.getOrderItemById(this.orderItemId).subscribe({
      next: (orderItem) => {
        this.orderItemForm.patchValue({
          productId: orderItem.product?.productId,
          quantity: orderItem.quantity,
          discountId: orderItem.discount?.discountId,
          price: orderItem.price
        });
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading order item', err);
        this.isLoading = false;
      }
    });
  }

  updatePrice(productId: number): void {
    const product = this.products.find(p => p.productId === productId);
    if (product) {
      this.orderItemForm.patchValue({
        price: product.price
      });
    }
  }

  onSubmit(): void {
    if (this.orderItemForm.invalid) {
      return;
    }

    const formValue = this.orderItemForm.getRawValue();
    const orderItem: OrderItem = {
      orderItemId: this.orderItemId,
      orderId: this.orderId,
      productId: formValue.productId,
      quantity: formValue.quantity,
      price: formValue.price,
      discountId: formValue.discountId
    };

    this.isLoading = true;

    if (this.isEditMode && this.orderItemId) {
      this.orderItemService.updateOrderItem(this.orderItemId, orderItem).subscribe({
        next: () => {
          this.snackBar.open('Элемент заказа успешно обновлен', 'Закрыть', { duration: 3000 });
          this.navigateBack();
        },
        error: (err) => {
          console.error('Error updating order item', err);
          this.snackBar.open('Ошибка при обновлении элемента заказа', 'Закрыть', { duration: 3000 });
          this.isLoading = false;
        }
      });
    } else {
      this.orderItemService.createOrderItem(orderItem).subscribe({
        next: () => {
          this.snackBar.open('Элемент заказа успешно создан', 'Закрыть', { duration: 3000 });
          this.navigateBack();
        },
        error: (err) => {
          console.error('Error creating order item', err);
          this.snackBar.open('Ошибка при создании элемента заказа', 'Закрыть', { duration: 3000 });
          this.isLoading = false;
        }
      });
    }
  }

  onDelete(): void {
    if (!this.orderItemId || !confirm('Вы уверены, что хотите удалить этот элемент заказа?')) {
      return;
    }

    this.isLoading = true;
    this.orderItemService.deleteOrderItem(this.orderItemId).subscribe({
      next: () => {
        this.snackBar.open('Элемент заказа успешно удален', 'Закрыть', { duration: 3000 });
        this.navigateBack();
      },
      error: (err) => {
        console.error('Error deleting order item', err);
        this.snackBar.open('Ошибка при удалении элемента заказа', 'Закрыть', { duration: 3000 });
        this.isLoading = false;
      }
    });
  }

  navigateBack(): void {
    this.router.navigate(['/clients', this.clientId, 'orders', this.orderId, 'order-items']);
  }
}