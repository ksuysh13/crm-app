import { inject, Injectable } from '@angular/core';
import { environment } from '../../enviroment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CookieService } from 'ngx-cookie-service'

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}auth`;

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor() {}

  http: HttpClient = inject(HttpClient);

  cookieService = inject(CookieService);

  userName = this.cookieService.get("login");

  password = this.cookieService.get("password");

  role = this.cookieService.get("role");

  login(login: string, password: string) {
    let httpOptions = {
        headers: new HttpHeaders({
            'Authorization': 'Basic ' + btoa(`${login}:${password}`)
        })
    }
    return this.http.get<string>(`${this.apiUrl}/login`, httpOptions);
  }

  saveLoginPassword(login: string, password: string, role: string) {
    this.cookieService.set("login", login);
    this.cookieService.set("password", password);
    this.cookieService.set("role", role);
  }

  getUserInfo() {
    let login = this.cookieService.get("login");
    let password = this.cookieService.get("password");
    let role = this.cookieService.get("role");
    return [login, password, role]
  }

  forgetMe() {
    this.cookieService.deleteAll();
  }

  isAuth() {
    return (this.userName && this.password && this.role);
  }
}
