import { Component, inject, OnInit } from '@angular/core';
import { ProductGroupService } from '../data/services/product-group.service';
import { ProductGroup } from '../data/models/ProductGroup';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../data/services/auth.service';

@Component({
  selector: 'app-product-group-list',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    RouterModule
  ],
  templateUrl: './product-group-list.component.html',
  styleUrls: ['./product-group-list.component.css']
})
export class ProductGroupListComponent implements OnInit {
  productGroups: ProductGroup[] = [];
  selectedGroupId: number | null = null;

  authService = inject(AuthService);

  constructor(
    private productGroupService: ProductGroupService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadProductGroups();
  }

  loadProductGroups(): void {
    this.productGroupService.getAllProductGroups().subscribe({
      next: (data) => this.productGroups = data,
      error: (err) => console.error('Error loading product groups', err)
    });
  }

  onCreateProductGroup(): void {
    this.router.navigate(['/product-groups/new']); 
  }

  toggleMenu(groupId: number | undefined): void {
    if (groupId !== undefined) {
      this.selectedGroupId = this.selectedGroupId === groupId ? null : groupId;
    }
  }

  editProductGroup(groupId: number | undefined): void {
    if (groupId !== undefined) {
      this.router.navigate(['/product-groups', groupId]);
      this.selectedGroupId = null;
    }
  }

  deleteProductGroup(groupId: number | undefined): void {
    if (groupId !== undefined) {
      if (confirm('Вы уверены, что хотите удалить эту группу продуктов?')) {
        this.productGroupService.deleteProductGroup(groupId).subscribe({
          next: () => {
            this.loadProductGroups();
            this.selectedGroupId = null;
          },
          error: (err) => console.error('Error deleting product group', err)
        });
      }
    }
  }

  viewProducts(groupId: number | undefined): void {
    if (groupId !== undefined) {
      this.router.navigate(['/product-groups', groupId, 'products']);
    }
  }
}