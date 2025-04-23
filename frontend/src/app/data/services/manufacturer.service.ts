import { inject, Injectable } from '@angular/core';
import { environment } from '../../enviroment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Manufacturer } from '../models/Manufacturer';

@Injectable({
  providedIn: 'root'
})
export class ManufacturerService {
  private apiUrl = `${environment.apiUrl}manufacturers`;

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor() {}

  http: HttpClient = inject(HttpClient);

  getAllManufacturers(): Observable<Manufacturer[]> {
    // const url = search ? `${this.apiUrl}?search=${search}` : `${this.apiUrl}`;
    return this.http.get<Manufacturer[]>(`${this.apiUrl}`);
  }

  getManufacturerById(id: number): Observable<Manufacturer> {
    return this.http.get<Manufacturer>(`${this.apiUrl}/${id}`);
  }

  createManufacturer(manufacturer: Manufacturer): Observable<Manufacturer> {
    return this.http.post<Manufacturer>(this.apiUrl, manufacturer);
  }

  updateManufacturer(id: number, manufacturer: Manufacturer): Observable<Manufacturer> {
    return this.http.put<Manufacturer>(`${this.apiUrl}/${id}`, manufacturer);
  }

  deleteManufacturer(manufacturerId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${manufacturerId}`);
  }

  getUnsoldProductsByManufacturer(manufacturerId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${manufacturerId}/unsold-products`);
  }

  deleteSoldProductsByManufacturer(manufacturerId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${manufacturerId}/sold-products`);
  }
}