import { Component, OnInit } from '@angular/core';
import { Discount } from '../data/models/Discount';
import { DiscountService } from '../data/services/discount.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-discount',
  standalone: true,
  imports: [],
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
}
