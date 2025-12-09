import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StudentProfileRequest, StudentProfileResponse } from '../models/student.model';

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  private baseUrl = 'http://localhost:8080/students';

  constructor(private http: HttpClient) {}

  createProfile(userId: number, data: StudentProfileRequest): Observable<StudentProfileResponse> {
    return this.http.post<StudentProfileResponse>(`${this.baseUrl}/${userId}/profile`, data);
  }

  getProfile(userId: number): Observable<StudentProfileResponse> {
    return this.http.get<StudentProfileResponse>(`${this.baseUrl}/${userId}/profile`);
  }
}