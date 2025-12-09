import { Component, ChangeDetectorRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MentorService } from '../../services/mentor.service';
import { AuthService } from '../../services/auth.service';
import { MentorshipService } from '../../services/mentorship.service';
import { MentorProfileResponse, MentorFilter } from '../../models/mentor.models';
import { MentorshipRequestDto } from '../../models/mentorship.model';

@Component({
  selector: 'app-find-mentor',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './find-mentor.html',
  styleUrls: ['./find-mentor.scss']
})
export class FindMentorComponent implements OnInit {
  
  filters: MentorFilter = {
    searchQuery: '',
    minExperience: 0,
    company: '',
    jobTitle: '',
    language: ''
  };

  mentors: MentorProfileResponse[] = [];
  isLoading = false;
  
  sentRequests: Set<number> = new Set();
  requestStatus: {[key: number]: 'IDLE' | 'SENDING' | 'SENT' | 'ERROR'} = {};

  showRequestModal = false;
  selectedMentorId: number | null = null;
  selectedMentorName: string = '';
  
  requestMessage = "I would like to connect for mentorship.";
  

  constructor(
    private mentorService: MentorService, 
    private mentorshipService: MentorshipService,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.onSearch(); 
  }

  onSearch() {
    this.isLoading = true;
    this.mentors = [];

    this.mentorService.searchMentors(this.filters).subscribe({
      next: (data) => {
        this.mentors = data;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error fetching mentors:', err);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  resetFilters() {
    this.filters = {
      searchQuery: '',
      minExperience: 0,
      company: '',
      jobTitle: '',
      language: ''
    };
    this.onSearch();
  }

  goBack() {
    this.router.navigate(['/student-dashboard']);
  }

  openRequestModal(mentorId: number, mentorName: string) {
    this.selectedMentorId = mentorId;
    this.selectedMentorName = mentorName;
    this.showRequestModal = true;
    
    this.requestMessage = `Hi ${mentorName}, I would like to connect for mentorship.`;
  }

  closeModal() {
    this.showRequestModal = false;
    this.selectedMentorId = null;
  }

  submitRequest() {
    if (!this.selectedMentorId) return;
    
    const mentorId = this.selectedMentorId;
    const studentId = Number(this.authService.getUserId());
    
    if (!studentId) {
        this.router.navigate(['/login']);
        return;
    }

    this.requestStatus[mentorId] = 'SENDING';
    this.closeModal();
    this.cdr.detectChanges();

    const requestData: MentorshipRequestDto = {
        studentId: studentId,
        mentorId: mentorId,
        message: this.requestMessage
    };

    this.mentorshipService.sendRequest(requestData).subscribe({
        next: (res) => {
            this.sentRequests.add(mentorId);
            this.requestStatus[mentorId] = 'SENT';
            this.cdr.detectChanges();
        },
        error: (err) => {
            alert("Failed to send request. You might already have a pending request.");
            this.requestStatus[mentorId] = 'ERROR';
            setTimeout(() => {
                this.requestStatus[mentorId] = 'IDLE';
                this.cdr.detectChanges();
            }, 3000);
            this.cdr.detectChanges();
        }
    });
  }

  isRequestSent(mentorId: number): boolean {
    return this.sentRequests.has(mentorId);
  }
}