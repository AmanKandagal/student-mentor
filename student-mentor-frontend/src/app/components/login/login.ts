import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { LoginRequest } from '../../models/auth.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.html',
  styles: [`
    /* Animation Keyframes */
    @keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
    @keyframes slideUp { from { opacity: 0; transform: translateY(20px); } to { opacity: 1; transform: translateY(0); } }
    @keyframes shake { 0%, 100% { transform: translateX(0); } 25% { transform: translateX(-5px); } 75% { transform: translateX(5px); } }
    
    .animate-fadeIn { animation: fadeIn 0.5s ease-out forwards; }
    .animate-slideUp { animation: slideUp 0.6s ease-out forwards; }
    .animate-shake { animation: shake 0.3s ease-in-out; }
  `]
})
export class LoginComponent {
  loginData: LoginRequest = { email: '', password: '' };
  isLoading = false;
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    if (!this.loginData.email || !this.loginData.password) {
      this.errorMessage = 'Please fill in all fields';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.authService.login(this.loginData).subscribe({
      next: (res) => {
        if (res.token) {
          localStorage.setItem('token', res.token);
        }

        const role = this.authService.getRole();
        
        if (role === 'MENTOR') {
          this.router.navigate(['/mentor-dashboard']);
        } else if (role === 'STUDENT') {
          this.router.navigate(['/student-dashboard']);
        } else {
          this.errorMessage = 'Role not recognized.';
        }
        
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Invalid email or password.';
        this.isLoading = false;
      }
    });
  }
}