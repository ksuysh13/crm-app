import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ManufacturerService } from '../data/services/manufacturer.service';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Manufacturer } from '../data/models/Manufacturer';
import { CommonModule } from '@angular/common';
import { ProductGroupService } from '../data/services/product-group.service';
import { ProductGroup } from '../data/models/ProductGroup';

@Component({
  selector: 'app-edit-product-group',
  imports: [CommonModule, ReactiveFormsModule],
  standalone: true,
  templateUrl: './edit-product-group.component.html',
  styleUrl: './edit-product-group.component.css'
})
export class EditProductGroupComponent implements OnInit {
  groupId: number | null = null;
  groupForm: FormGroup;
  isEditMode = false;

  constructor(
    private route: ActivatedRoute,
    public router: Router,
    private productGroupService: ProductGroupService,
    private fb: FormBuilder
  ) {
    this.groupForm = this.fb.group({
      groupName: ['', Validators.required],
      description: ['']
    });
  }

  ngOnInit(): void {
    this.groupId = +this.route.snapshot.paramMap.get('groupId')!;
    this.isEditMode = !!this.groupId;

    if (this.isEditMode) {
      this.loadProductGroupData(this.groupId);
    }
  }

  loadProductGroupData(groupId: number): void {
    this.productGroupService.getProductGroupById(groupId).subscribe(group => {
      this.groupForm.patchValue({
        groupName: group.groupName,
        description: group.description
      });
    });
  }

  onSubmit(): void {
    if (this.groupForm.invalid) {
      return;
    }

    const groupData: ProductGroup = {
      groupId: this.groupId || undefined,
      groupName: this.groupForm.value.groupName,
      description: this.groupForm.value.description
    };

    if (this.isEditMode) {
      this.productGroupService.updateProductGroup(this.groupId!, groupData).subscribe(() => {
        this.router.navigate(['/product-groups']);
      });
    } else {
      this.productGroupService.createProductGroup(groupData).subscribe(() => {
        this.router.navigate(['/product-groups']);
      });
    }
  }

}
