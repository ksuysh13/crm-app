import { Component, inject, OnInit } from '@angular/core';
import { ClientService } from '../data/services/client.service';
import { Client } from '../data/models/Client';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';
// import { ClientEditDialogComponent } from '../client-edit-dialog/client-edit-dialog.component';
import { PhonePipe } from './pipes/phone.pipe'; 
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../data/services/auth.service';

@Component({
  selector: 'app-client-list',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    PhonePipe,
    RouterModule
  ],
  templateUrl: './client-list.component.html',
  styleUrls: ['./client-list.component.css']
})
export class ClientListComponent implements OnInit {
  clients: Client[] = [];
  selectedClientId: number | null = null;

  authService = inject(AuthService);

  constructor(
    private clientService: ClientService,
    private dialog: MatDialog,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadClients();
  }

  loadClients(): void {
    this.clientService.getAllClients().subscribe({
      next: (data) => this.clients = data,
      error: (err) => console.error('Error loading clients', err)
    });
  }

  onCreateClient(): void {
    this.router.navigate(['/clients/new']); 
  }

  toggleMenu(clientId: number | undefined): void {
    if (clientId !== undefined) {
      this.selectedClientId = this.selectedClientId === clientId ? null : clientId;
    }
  }

  editClient(clientId: number | undefined): void {
    if (clientId !== undefined) {
      this.router.navigate(['/clients', clientId]);
      this.selectedClientId = null; // Закрываем меню при переходе на редактирование
    }
  }
  
  deleteClient(clientId: number | undefined): void {
    if (clientId !== undefined) {
      if (confirm('Вы уверены, что хотите удалить этого клиента?')) {
        this.clientService.deleteClient(clientId).subscribe({
          next: () => {
            this.loadClients();
            this.selectedClientId = null;
          },
          error: (err) => console.error('Error deleting client', err)
        });
      }
    }
  }

  viewClientOrders(clientId: number | undefined): void {
    if (clientId !== undefined) {
      this.router.navigate(['/clients', clientId, 'orders']);
    }
  }

  // editClient(client: Client): void {
  //   const dialogRef = this.dialog.open(ClientEditDialogComponent, {
  //     width: '500px',
  //     data: { ...client }
  //   });

  //   dialogRef.afterClosed().subscribe(result => {
  //     if (result) {
  //       this.loadClients();
  //     }
  //   });
  // }

  // deleteClient(id: number): void {
  //   if (confirm('Вы уверены, что хотите удалить этого клиента?')) {
  //     this.clientService.deleteClient(id).subscribe({
  //       next: () => this.loadClients(),
  //       error: (err) => console.error('Error deleting client', err)
  //     });
  //   }
  // }
}