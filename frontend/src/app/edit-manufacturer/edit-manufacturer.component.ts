import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ManufacturerService } from '../data/services/manufacturer.service';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Manufacturer } from '../data/models/Manufacturer';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-edit-manufacturer',
  imports: [CommonModule, ReactiveFormsModule],
  standalone: true,
  templateUrl: './edit-manufacturer.component.html',
  styleUrl: './edit-manufacturer.component.css'
})
export class EditManufacturerComponent implements OnInit {
  manufacturerId: number | null = null;
  manufacturerForm: FormGroup;
  isEditMode = false;

  constructor(
    private route: ActivatedRoute,
    public router: Router,
    private manufacturerService: ManufacturerService,
    private fb: FormBuilder
  ) {
    this.manufacturerForm = this.fb.group({
      manufacturerName: ['', Validators.required],
      country: ['']
    });
  }

  ngOnInit(): void {
    this.manufacturerId = +this.route.snapshot.paramMap.get('manufacturerId')!;
    this.isEditMode = !!this.manufacturerId;

    if (this.isEditMode) {
      this.loadManufacturerData(this.manufacturerId);
    }
  }

  loadManufacturerData(manufacturerId: number): void {
    this.manufacturerService.getManufacturerById(manufacturerId).subscribe(manufacturer => {
      this.manufacturerForm.patchValue({
        manufacturerName: manufacturer.manufacturerName,
        country: manufacturer.country
      });
    });
  }

  onSubmit(): void {
    if (this.manufacturerForm.invalid) {
      return;
    }

    const manufacturerData: Manufacturer = {
      manufacturerId: this.manufacturerId || undefined,
      manufacturerName: this.manufacturerForm.value.manufacturerName,
      country: this.manufacturerForm.value.country
    };

    if (this.isEditMode) {
      this.manufacturerService.updateManufacturer(this.manufacturerId!, manufacturerData).subscribe(() => {
        this.router.navigate(['/manufacturers']);
      });
    } else {
      this.manufacturerService.createManufacturer(manufacturerData).subscribe(() => {
        this.router.navigate(['/manufacturers']);
      });
    }
  }
}