import { Component, OnInit } from '@angular/core';
import { ClientService } from '../data/services/client.service';
import { Client } from '../data/models/Client';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-client-list',
  imports: [CommonModule],
  templateUrl: './client-list.component.html',
  styleUrl: './client-list.component.css',

  standalone: true, 
})
export class ClientListComponent {
  clients: Client[] = [];

  constructor(private clientService: ClientService) { }

  ngOnInit(): void {
    this.clientService.getAllClients().subscribe(
      (data: Client[]) => {
        this.clients = data;
      },
      (error) => {
        console.error('Error fetching projects', error);
      }
    );
  }
}
