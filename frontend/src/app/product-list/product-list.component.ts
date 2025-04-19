// product-list.component.ts
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../data/services/product.service';
import { Product } from '../data/models/Product';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  groupId!: number;
  selectedProductId: number | null = null;

  constructor(
    private productService: ProductService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.groupId = +this.route.snapshot.paramMap.get('groupId')!;
    this.loadProducts();
  }

  loadProducts(): void {
    this.productService.getProductsByGroupId(this.groupId).subscribe({
      next: (data) => this.products = data,
      error: (err) => console.error('Error loading products', err)
    });
  }

  onCreateProduct(): void {
    this.router.navigate(['/product-groups', this.groupId, 'products', 'new']);
  }

  toggleMenu(productId: number | undefined): void {
    if (productId !== undefined) {
      this.selectedProductId = this.selectedProductId === productId ? null : productId;
    }
  }

  editProduct(productId: number | undefined): void {
    if (productId !== undefined) {
      this.router.navigate(['/product-groups', this.groupId, 'products', productId]);
      this.selectedProductId = null;
    }
  }

  deleteProduct(productId: number | undefined): void {
    if (productId !== undefined) {
      if (confirm('Вы уверены, что хотите удалить этот продукт?')) {
        this.productService.deleteProduct(productId).subscribe({
          next: () => {
            this.loadProducts();
            this.selectedProductId = null;
          },
          error: (err) => console.error('Error deleting product', err)
        });
      }
    }
  }
}