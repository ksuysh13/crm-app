import { Component, OnInit } from '@angular/core';
import { Discount } from '../data/models/Discount';
import { DiscountService } from '../data/services/discount.service';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-discount',
  standalone: true,
  imports: [
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    CommonModule,
    DatePipe
  ],
  templateUrl: './discount.component.html',
  styleUrl: './discount.component.css'
})
export class DiscountComponent implements OnInit {
  discounts: Discount[] = [];

  constructor(
    private discountService: DiscountService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadDiscounts();
  }

  loadDiscounts(): void {
    this.discountService.getAllDiscounts().subscribe({
      next: (data) => this.discounts = data,
      error: (err) => console.error('Error loading discounts', err)
    });
  }

  onCreateDiscount(): void {
    this.router.navigate(['/discounts/new']);
  }

  editDiscount(discountId: number | undefined): void {
    if (discountId !== undefined) {
      this.router.navigate(['/discounts', discountId]);
    }
  }

  deleteDiscount(discountId: number | undefined): void {
    if (discountId !== undefined) {
      if (confirm('Вы уверены, что хотите удалить эту скидку?')) {
        this.discountService.deleteDiscount(discountId).subscribe({
          next: () => {
            this.loadDiscounts();
          },
          error: (err) => console.error('Error deleting discount', err)
        });
      }
    }
  }
}