import { Component, OnInit, ChangeDetectorRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { forkJoin } from 'rxjs'; 
import { AuthService } from '../../services/auth.service';
import { MentorshipService } from '../../services/mentorship.service';
import { MentorshipResponse } from '../../models/mentorship.model';
import { ChatService } from '../../services/chat.service';
import { ChatComponent } from '../chat/chat';

@Component({
  selector: 'app-mentor-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, ChatComponent],
  templateUrl: './mentor-dashboard.html',
  styleUrls: ['./mentor-dashboard.scss']
})
export class MentorDashboardComponent implements OnInit {

  pendingRequests: MentorshipResponse[] = [];
  acceptedMentees: MentorshipResponse[] = [];
  isLoading = false;
  
  showChat = false;
  chatTargetName = '';
  selectedChatUserId = '';

  chatService = inject(ChatService);
  authService = inject(AuthService);
  mentorshipService = inject(MentorshipService); 

  constructor(
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.loadData();

    const myId = this.authService.getUserId();
    if (myId) {
      this.chatService.connect(myId);
    }
  }

  loadData() {
    const userId = this.authService.getUserId();
    if (!userId) {
      this.router.navigate(['/login']);
      return;
    }
    
    this.isLoading = true;
    const mentorId = Number(userId); 

    forkJoin({
      pending: this.mentorshipService.getPendingRequests(mentorId),
      accepted: this.mentorshipService.getAcceptedRequests(mentorId)
    }).subscribe({
      next: (results) => {
        console.log('✅ Data Loaded:', results);
        this.pendingRequests = results.pending;
        this.acceptedMentees = results.accepted;
        
        this.isLoading = false; 
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('❌ Error loading dashboard data:', err);
        
        if (err.status === 0) {
          alert('Could not connect to backend. Is it running on port 8080?');
        }
        
        this.isLoading = false; 
        this.cdr.detectChanges();
      }
    });
  }

  handleAccept(requestId: number) {
    if(!confirm("Accept this student request?")) return;
    
    this.mentorshipService.acceptRequest(requestId).subscribe({
      next: () => {
        // Optimistic UI Update
        const request = this.pendingRequests.find(r => r.requestId === requestId);
        if (request) {
          this.pendingRequests = this.pendingRequests.filter(r => r.requestId !== requestId);
          request.status = 'ACCEPTED';
          this.acceptedMentees.push(request);
        }
        this.cdr.detectChanges();
      },
      error: (err) => alert("Failed to accept request.")
    });
  }

  handleDecline(requestId: number) {
    if(!confirm("Decline this request?")) return;
    
    this.mentorshipService.declineRequest(requestId).subscribe({
      next: () => {
        this.pendingRequests = this.pendingRequests.filter(r => r.requestId !== requestId);
        this.cdr.detectChanges();
      },
      error: (err) => alert("Failed to decline request.")
    });
  }

  openChat(studentId: number, name: string) {
    this.chatTargetName = name;
    this.selectedChatUserId = studentId.toString();
    this.showChat = true;
    this.chatService.setActiveChat(this.selectedChatUserId);
  }

  closeChat() {
    this.showChat = false;
    this.chatTargetName = '';
    this.selectedChatUserId = '';
    this.chatService.clearActiveChat();
  }

  logout() {
    this.chatService.disconnect();
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}