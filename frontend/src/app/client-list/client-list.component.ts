import { Component, inject, OnInit } from '@angular/core';
import { ClientService } from '../data/services/client.service';
import { Client } from '../data/models/Client';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';
import { PhonePipe } from './pipes/phone.pipe';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../data/services/auth.service';
import { OrderService } from '../data/services/order.service';
import { forkJoin } from 'rxjs';
import { MatSelectModule } from '@angular/material/select';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-client-list',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    PhonePipe,
    RouterModule,
    MatSelectModule,
    FormsModule,
    DatePipe
  ],
  templateUrl: './client-list.component.html',
  styleUrls: ['./client-list.component.css']
})
export class ClientListComponent implements OnInit {
  clients: Client[] = [];
  filteredClients: Client[] = [];
  selectedClientId: number | null = null;
  clientOrdersInfo: Record<number, {count: number, total: number, recentCount: number}> = {};
  
  sortOptions = [
    { value: 'alphabet', label: 'По алфавиту' },
    { value: 'ordersCount', label: 'По количеству заказов' },
    { value: 'totalAmount', label: 'По сумме заказов' },
    { value: 'recentOrders', label: 'По заказам за выбранный период' }
  ];
  selectedSort = 'alphabet';
  
  periodOptions = [
    { value: 'week', label: 'За неделю' },
    { value: 'month', label: 'За месяц' },
    { value: 'year', label: 'За год' },
    { value: 'all', label: 'За все время' }
  ];
  selectedPeriod = 'all';

  authService = inject(AuthService);

  constructor(
    private clientService: ClientService,
    private orderService: OrderService,
    private dialog: MatDialog,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadClients();
  }

  loadClients(): void {
    this.clientService.getAllClients().subscribe({
      next: (clients) => {
        this.clients = clients;
        this.filteredClients = [...clients];
        this.loadOrdersInfo(clients);
      },
      error: (err) => console.error('Error loading clients', err)
    });
  }

  loadOrdersInfo(clients: Client[]): void {
    const requests = clients.map(client => 
      this.orderService.getOrdersByClientId(client.clientId!)
    );

    forkJoin(requests).subscribe({
      next: (ordersArrays) => {
        const now = new Date();
        ordersArrays.forEach((orders, index) => {
          const clientId = clients[index].clientId!;
          
          // Фильтрация заказов по периоду
          const recentOrders = orders.filter(order => {
            const orderDate = new Date(order.orderDate);
            switch(this.selectedPeriod) {
              case 'week':
                return this.getDaysDiff(now, orderDate) <= 7;
              case 'month':
                return this.getDaysDiff(now, orderDate) <= 30;
              case 'year':
                return this.getDaysDiff(now, orderDate) <= 365;
              default:
                return true;
            }
          });

          this.clientOrdersInfo[clientId] = {
            count: orders.length,
            total: orders.reduce((sum, order) => sum + (order.totalAmount || 0), 0),
            recentCount: recentOrders.length
          };
        });
        this.sortAndFilterClients();
      },
      error: (err) => console.error('Error loading orders info', err)
    });
  }

  getDaysDiff(date1: Date, date2: Date): number {
    const diffTime = Math.abs(date1.getTime() - date2.getTime());
    return Math.ceil(diffTime / (1000 * 60 * 60 * 24)); 
  }

  sortAndFilterClients(): void {
    // Сначала сортировка
    switch (this.selectedSort) {
      case 'alphabet':
        this.filteredClients.sort((a, b) => 
          `${a.lastName} ${a.firstName}`.localeCompare(`${b.lastName} ${b.firstName}`)
        );
        break;
      case 'ordersCount':
        this.filteredClients.sort((a, b) => 
          (this.clientOrdersInfo[b.clientId!]?.count || 0) - 
          (this.clientOrdersInfo[a.clientId!]?.count || 0)
        );
        break;
      case 'totalAmount':
        this.filteredClients.sort((a, b) => 
          (this.clientOrdersInfo[b.clientId!]?.total || 0) - 
          (this.clientOrdersInfo[a.clientId!]?.total || 0)
        );
        break;
      case 'recentOrders':
        this.filteredClients.sort((a, b) => 
          (this.clientOrdersInfo[b.clientId!]?.recentCount || 0) - 
          (this.clientOrdersInfo[a.clientId!]?.recentCount || 0)
        );
        break;
    }

    // Затем фильтрация по периоду, если выбрана сортировка по активным заказам
    if (this.selectedSort === 'recentOrders') {
      this.filteredClients = this.filteredClients.filter(client => 
        this.clientOrdersInfo[client.clientId!]?.recentCount > 0
      );
    }
  }

  onPeriodChange(): void {
    this.loadOrdersInfo(this.clients);
  }

  onSortChange(): void {
    this.sortAndFilterClients();
  }

  // Остальные методы остаются без изменений
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
      this.selectedClientId = null;
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
}