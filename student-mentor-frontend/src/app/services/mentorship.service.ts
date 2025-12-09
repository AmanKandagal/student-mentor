import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MentorshipResponse, MentorshipRequestDto } from '../models/mentorship.model';

@Injectable({
  providedIn: 'root'
})
export class MentorshipService {
  
  private http = inject(HttpClient);
  // Ensure this matches your Spring Boot Controller path exactly
  private baseUrl = 'http://localhost:8080/requests'; 

  constructor() { }

  // 1. Student: Send a request
  sendRequest(data: MentorshipRequestDto): Observable<MentorshipResponse> {
    return this.http.post<MentorshipResponse>(`${this.baseUrl}/send`, data);
  }

  // 2. Student: Get my requests
  getMyRequests(studentId: number): Observable<MentorshipResponse[]> {
    return this.http.get<MentorshipResponse[]>(`${this.baseUrl}/student/${studentId}`);
  }

  // 3. Mentor: Get pending requests
  getPendingRequests(mentorId: number): Observable<MentorshipResponse[]> {
    return this.http.get<MentorshipResponse[]>(`${this.baseUrl}/mentor/${mentorId}/pending`);
  }

  // 4. Mentor: Get ACCEPTED requests
  getAcceptedRequests(mentorId: number): Observable<MentorshipResponse[]> {
    return this.http.get<MentorshipResponse[]>(`${this.baseUrl}/mentor/${mentorId}/accepted`);
  }

  // 5. Mentor: Accept
  acceptRequest(requestId: number): Observable<MentorshipResponse> {
    return this.http.patch<MentorshipResponse>(`${this.baseUrl}/${requestId}/accept`, {}); 
    // Note: Used PUT or PATCH depending on your backend. Standard is usually PUT/PATCH for updates.
  }

  // 6. Mentor: Decline
  declineRequest(requestId: number): Observable<MentorshipResponse> {
    return this.http.patch<MentorshipResponse>(`${this.baseUrl}/${requestId}/decline`, {});
  }
}