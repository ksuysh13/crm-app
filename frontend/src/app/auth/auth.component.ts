import { Component, inject } from '@angular/core';
import { AuthService } from '../data/services/auth.service';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-auth',
  imports: [ReactiveFormsModule],
  standalone: true,
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.css'
})
export class AuthComponent {

  authService = inject(AuthService);

  form = new FormGroup({
    login: new FormControl('', [Validators.required, Validators.minLength(1)]),
    password: new FormControl('', [Validators.required, Validators.minLength(1)])
  })

  login() {
    if (this.form.valid) {
      this.authService.login(this.form.value.login!, this.form.value.password!).subscribe({
        next: (role) => {
          this.authService.saveLoginPassword(this.form.value.login!, this.form.value.password!, role);
        },
        error: (err) => {
          console.log("Неверные данные: логин или пароль");
        }
      });
    }
  }
}
