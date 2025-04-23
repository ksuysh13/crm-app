import { Component, inject, OnInit } from '@angular/core';
import { ManufacturerService } from '../data/services/manufacturer.service';
import { Manufacturer } from '../data/models/Manufacturer';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../data/services/auth.service';

@Component({
  selector: 'app-manufacturer-list',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    RouterModule
  ],
  templateUrl: './manufacturer-list.component.html',
  styleUrls: ['./manufacturer-list.component.css']
})
export class ManufacturerListComponent implements OnInit {
  manufacturers: Manufacturer[] = [];
  searchTerm: string = '';
  selectedManufacturerId: number | null = null;

  authService = inject(AuthService);

  constructor(
    private manufacturerService: ManufacturerService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadManufacturers();
  }

  loadManufacturers(): void {
    this.manufacturerService.getAllManufacturers().subscribe({
      next: (data) => this.manufacturers = data,
      error: (err) => console.error('Error loading manufacturers', err)
    });
  }

  onCreateManufacturer(): void {
    this.router.navigate(['/manufacturers/new']);
  }

  toggleMenu(manufacturerId: number | undefined): void {
    if (manufacturerId !== undefined) {
      this.selectedManufacturerId = this.selectedManufacturerId === manufacturerId ? null : manufacturerId;
    }
  }

  editManufacturer(manufacturerId: number | undefined): void {
    if (manufacturerId !== undefined) {
      this.router.navigate(['/manufacturers', manufacturerId]);
      this.selectedManufacturerId = null;
    }
  }

  deleteManufacturer(manufacturerId: number | undefined): void {
    if (manufacturerId !== undefined) {
      if (confirm('Вы уверены, что хотите удалить этого производителя?')) {
        this.manufacturerService.deleteManufacturer(manufacturerId).subscribe({
          next: () => {
            this.loadManufacturers();
            this.selectedManufacturerId = null;
          },
          error: (err) => console.error('Error deleting manufacturer', err)
        });
      }
    }
  }

  onSearch(): void {
    this.loadManufacturers();
  }
}