import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MentorProfileResponse, MentorProfileRequest, MentorFilter } from '../models/mentor.models';

@Injectable({
  providedIn: 'root'
})
export class MentorService {
  
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/mentors'; 

  constructor() { }

  createProfile(userId: number, data: MentorProfileRequest): Observable<MentorProfileResponse> {
    return this.http.post<MentorProfileResponse>(`${this.baseUrl}/${userId}/profile`, data);
  }

  getProfile(userId: number): Observable<MentorProfileResponse> {
    return this.http.get<MentorProfileResponse>(`${this.baseUrl}/${userId}/profile`);
  }

  searchMentors(filters: MentorFilter): Observable<MentorProfileResponse[]> {
    let params = new HttpParams();

    if (filters.searchQuery) params = params.set('skill', filters.searchQuery);
    
    if (filters.company) params = params.set('company', filters.company);
    
    if (filters.minExperience > 0) params = params.set('minExp', filters.minExperience);
    if (filters.jobTitle) params = params.set('jobTitle', filters.jobTitle);
    
    if (filters.language) params = params.set('languages', filters.language);

    return this.http.get<MentorProfileResponse[]>(`${this.baseUrl}/search`, { params });
  }

}