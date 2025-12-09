import { Component, OnInit, ChangeDetectorRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { MentorshipService } from '../../services/mentorship.service';
import { MentorshipResponse } from '../../models/mentorship.model';
import { ChatService } from '../../services/chat.service'; 
import { ChatComponent } from '../chat/chat'; 

@Component({
  selector: 'app-student-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, ChatComponent],
  templateUrl: './student-dashboard.html',
  styleUrls: ['./student-dashboard.scss']
})
export class StudentDashboardComponent implements OnInit {
  myRequests: MentorshipResponse[] = [];
  isLoading = false;

  showChat = false;
  chatTargetName = '';
  selectedChatUserId = '';

  // Inject Services
  chatService = inject(ChatService);
  authService = inject(AuthService);

  constructor(
    private mentorshipService: MentorshipService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.loadMyRequests();
    
    const myId = this.authService.getUserId();
    if (myId) {
      this.chatService.connect(myId);
    }
  }

  loadMyRequests() {
    const userId = this.authService.getUserId();
    if (!userId) return;
    this.isLoading = true;
    this.mentorshipService.getMyRequests(Number(userId)).subscribe({
      next: (data) => {
        this.myRequests = data;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error fetching requests', err);
        this.isLoading = false;
      }
    });
  }

  openChat(mentorId: number, name: string) {
    this.chatTargetName = name;
    this.selectedChatUserId = mentorId.toString();
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