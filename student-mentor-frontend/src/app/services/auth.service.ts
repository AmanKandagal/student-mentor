import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { jwtDecode } from 'jwt-decode';
import { LoginRequest, RegisterRequest, AuthResponse } from '../models/auth.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8080/auth';

  constructor(private http: HttpClient) {}

  register(data: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/register`, data);
  }

  login(data: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/login`, data).pipe(
      tap(response => {
        if (response.token) {
          localStorage.setItem('token', response.token);
          
          
          try {
            const decoded: any = jwtDecode(response.token);
            localStorage.setItem('role', decoded.role);
            localStorage.setItem('userId', decoded.userId || decoded.id || decoded.sub);
          } catch (e) {
            console.error('Error decoding token', e);
          }
        }
      })
    );
  }

  logout() {
    localStorage.clear();
  }

  getToken() { return localStorage.getItem('token'); }
  getRole() { return localStorage.getItem('role'); }
  getUserId() { return localStorage.getItem('userId'); }
  isLoggedIn() { return !!localStorage.getItem('token'); }
}