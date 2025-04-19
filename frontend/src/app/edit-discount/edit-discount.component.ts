import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { DiscountService } from '../data/services/discount.service';
import { Discount } from '../data/models/Discount';

@Component({
  selector: 'app-edit-discount',
  imports: [CommonModule, ReactiveFormsModule],
  standalone: true,
  templateUrl: './edit-discount.component.html',
  styleUrl: './edit-discount.component.css'
})
export class EditDiscountComponent implements OnInit {
  discountId: number | null = null;
  discountForm: FormGroup;
  isEditMode = false;

  constructor(
    private route: ActivatedRoute,
    public router: Router,
    private discountService: DiscountService,
    private fb: FormBuilder
  ) {
    this.discountForm = this.fb.group({
      discountPercentage: ['', [Validators.required, Validators.min(1), Validators.max(100)]],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('discountId');
    this.discountId = id ? +id : null;
    this.isEditMode = !!this.discountId;

    if (this.isEditMode && this.discountId !== null) {
      this.loadDiscountData(this.discountId);
    }
  }

  loadDiscountData(discountId: number): void {
    this.discountService.getDiscountById(discountId).subscribe({
      next: (discount) => {
        this.discountForm.patchValue({
          discountPercentage: discount.discountPercentage,
          startDate: this.formatDateForInput(discount.startDate),
          endDate: this.formatDateForInput(discount.endDate)
        });
      },
      error: (err) => console.error('Error loading discount', err)
    });
  }

  onSubmit(): void {
    if (this.discountForm.invalid || this.discountId === null && this.isEditMode) {
      return;
    }

    const discountData: Discount = {
      discountId: this.discountId || undefined,
      discountPercentage: this.discountForm.value.discountPercentage,
      startDate: new Date(this.discountForm.value.startDate),
      endDate: new Date(this.discountForm.value.endDate)
    };

    if (this.isEditMode && this.discountId !== null) {
      this.discountService.updateDiscount(this.discountId, discountData).subscribe({
        next: () => this.router.navigate(['/discounts']),
        error: (err) => console.error('Error updating discount', err)
      });
    } else {
      this.discountService.createDiscount(discountData).subscribe({
        next: () => this.router.navigate(['/discounts']),
        error: (err) => console.error('Error creating discount', err)
      });
    }
  }

  private formatDateForInput(date: Date | string): string {
    const d = new Date(date);
    return d.toISOString().substring(0, 10);
  }
}