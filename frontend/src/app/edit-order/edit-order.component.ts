import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { OrderService } from '../data/services/order.service';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Order } from '../data/models/Order';
import { CommonModule } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';

@Component({
  selector: 'app-edit-order',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatInputModule,
    MatFormFieldModule,
    MatCheckboxModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  templateUrl: './edit-order.component.html',
  styleUrls: ['./edit-order.component.css']
})
export class EditOrderComponent implements OnInit {
  orderId: number | null = null;
  clientId!: number;
  orderForm: FormGroup;
  isEditMode = false;

  constructor(
    private route: ActivatedRoute,
    public router: Router,
    private orderService: OrderService,
    private fb: FormBuilder
  ) {
    this.orderForm = this.fb.group({
      orderDate: ['', Validators.required],
      totalAmount: [{value: '', disabled: true}],
      isCompleted: [false]
    });
  }

  ngOnInit(): void {
    this.clientId = +this.route.snapshot.paramMap.get('clientId')!;
    this.orderId = +this.route.snapshot.paramMap.get('orderId')!;
    this.isEditMode = !!this.orderId;

    if (this.isEditMode) {
      this.loadOrderData();
    } else {
      this.orderForm.patchValue({
        orderDate: new Date().toISOString().split('T')[0]
      });
    }
  }

  loadOrderData(): void {
    this.orderService.getOrderById(this.clientId, this.orderId!).subscribe(order => {
      this.orderForm.patchValue({
        orderDate: order.orderDate,
        totalAmount: order.totalAmount,
        isCompleted: order.isCompleted
      });
    });
  }

  onSubmit(): void {
    if (this.orderForm.invalid) {
      return;
    }

    const orderData: Order = {
      orderId: this.orderId || undefined,
      orderDate: this.orderForm.value.orderDate,
      totalAmount: this.orderForm.get('totalAmount')?.value || 0,
      isCompleted: this.orderForm.value.isCompleted,
      clientId: this.clientId
    };

    if (this.isEditMode) {
      this.orderService.updateOrder(this.clientId, this.orderId!, orderData).subscribe(() => {
        this.router.navigate(['/clients', this.clientId, 'orders']);
      });
    } else {
      this.orderService.createOrder(this.clientId, orderData).subscribe(() => {
        this.router.navigate(['/clients', this.clientId, 'orders']);
      });
    }
  }
}