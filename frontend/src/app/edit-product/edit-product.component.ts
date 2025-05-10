import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ProductService } from '../data/services/product.service';
import { Product } from '../data/models/Product';
import { ManufacturerService } from '../data/services/manufacturer.service';
import { Manufacturer } from '../data/models/Manufacturer';
import { ProductGroupService } from '../data/services/product-group.service';
import { ProductGroup } from '../data/models/ProductGroup';

@Component({
  selector: 'app-edit-product',
  imports: [CommonModule, ReactiveFormsModule],
  standalone: true,
  templateUrl: './edit-product.component.html',
  styleUrl: './edit-product.component.css'
})
export class EditProductComponent implements OnInit {
  productId: number | null = null;
  groupId!: number;
  productForm: FormGroup;
  isEditMode = false;
  manufacturers: Manufacturer[] = [];
  productGroups: ProductGroup[] = [];
  isLoading = true;

  constructor(
    private route: ActivatedRoute,
    public router: Router,
    private productService: ProductService,
    private manufacturerService: ManufacturerService,
    private productGroupService: ProductGroupService,
    private fb: FormBuilder
  ) {
    this.productForm = this.fb.group({
      productName: ['', Validators.required],
      description: [''],
      price: ['', [Validators.required, Validators.min(0)]],
      stockQuantity: ['', [Validators.required, Validators.min(0)]],
      manufacturerId: ['', Validators.required],
      groupId: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.groupId = +this.route.snapshot.paramMap.get('groupId')!;
    this.productId = +this.route.snapshot.paramMap.get('productId')!;
    this.isEditMode = !!this.productId;

    this.loadManufacturers();
    this.loadProductGroups();

    if (this.isEditMode) {
      this.loadProductData(this.productId);
    } else {
      this.productForm.patchValue({ groupId: this.groupId });
      this.isLoading = false;
    }
  }

  loadManufacturers(): void {
    this.manufacturerService.getAllManufacturers().subscribe(data => {
      this.manufacturers = data;
      this.checkLoadingComplete();
    });
  }

  loadProductGroups(): void {
    this.productGroupService.getAllProductGroups().subscribe(data => {
      this.productGroups = data;
      this.checkLoadingComplete();
    });
  }

  loadProductData(productId: number): void {
    this.productService.getProductById(productId).subscribe({
      next: (product) => {
        this.productForm.patchValue({
          productName: product.productName,
          description: product.description,
          price: product.price,
          stockQuantity: product.stockQuantity,
          manufacturerId: product.manufacturerId,
          groupId: product.groupId
        });
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading product:', err);
        this.isLoading = false;
        this.router.navigate(['/product-groups', this.groupId, 'products']);
      }
    });
  }

  checkLoadingComplete(): void {
    if (!this.isEditMode && this.manufacturers.length && this.productGroups.length) {
      this.isLoading = false;
    }
  }

  onSubmit(): void {
    if (this.productForm.invalid || this.isLoading) {
      return;
    }

    const productData: Product = {
      productId: this.productId || undefined,
      ...this.productForm.value
    };

    if (this.isEditMode) {
      this.productService.updateProduct(productData).subscribe(() => {
        this.router.navigate(['/product-groups', this.groupId, 'products']);
      });
    } else {
      this.productService.createProduct(productData).subscribe(() => {
        this.router.navigate(['/product-groups', this.groupId, 'products']);
      });
    }
  }
}