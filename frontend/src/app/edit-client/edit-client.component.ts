import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ClientService } from '../data/services/client.service';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Client } from '../data/models/Client';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-edit-client',
  imports: [CommonModule, ReactiveFormsModule],
  standalone: true,
  templateUrl: './edit-client.component.html',
  styleUrl: './edit-client.component.css'
})
export class EditClientComponent implements OnInit {
  clientId: number | null = null;
  projectForm: FormGroup; 
  isEditMode = false;

  constructor(
    private route: ActivatedRoute,
    public router: Router,
    private clientService: ClientService,
    private fb: FormBuilder
  ) {
    this.projectForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.required],
      phone: ['', Validators.required],
      address: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.clientId = +this.route.snapshot.paramMap.get('clientId')!; 
    this.isEditMode = !!this.clientId;

    if (this.isEditMode) {
      this.loadProjectData(this.clientId);
    }
  }

  loadProjectData(clientId: number): void {
    this.clientService.getClientById(clientId).subscribe(client => {  
      this.projectForm.patchValue({
        firstName: client.firstName,
        lastName: client.lastName,
        email: client.email,
        phone: client.phone,
        address: client.address
      });
    });
  }

  onSubmit(): void {
    if (this.projectForm.invalid) {
      return;
    }

    const clientData: Client = {
      clientId: this.clientId || undefined,
      firstName: this.projectForm.value.firstName,
      lastName: this.projectForm.value.lastName,
      email: this.projectForm.value.email,
      phone: this.projectForm.value.phone,
      address: this.projectForm.value.address,
    };

    if (this.isEditMode) {
      this.clientService.updateClient(this.clientId!, clientData).subscribe(() => {
        this.router.navigate(['/clients']);
      });
    } else {
      this.clientService.createClient(clientData).subscribe(() => {
        this.router.navigate(['/clients']);
      });
    }
  }

}
