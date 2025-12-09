import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { MentorService } from '../../services/mentor.service';
import { MentorProfileRequest, MentorProfileResponse } from '../../models/mentor.models';

@Component({
  selector: 'app-mentor-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './mentor-profile.html',
  styleUrls: ['./mentor-profile.scss']
})
export class MentorProfileComponent implements OnInit {
  mode: 'loading' | 'create' | 'view' = 'loading';
  profileData: MentorProfileResponse | null = null;
  isSaving = false;
  
  formData: MentorProfileRequest = {
    company: '',
    jobTitle: '',
    bio: '',
    yearsOfExperience: 0,
    skills: '',
    languages: '',
    linkedinUrl: ''
  };

  constructor(
    private authService: AuthService, 
    private mentorService: MentorService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.loadProfile();
  }

  loadProfile() {
    const userId = Number(this.authService.getUserId());
    if (!userId) {
      this.router.navigate(['/login']);
      return;
    }

    this.mentorService.getProfile(userId).subscribe({
      next: (res) => {
        this.profileData = res;
        this.mode = 'view';
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.mode = 'create';
        this.cdr.detectChanges();
      }
    });
  }

  enableEditMode() {
    if (this.profileData) {
      this.formData = {
        company: this.profileData.company,
        jobTitle: this.profileData.jobTitle,
        bio: this.profileData.bio,
        yearsOfExperience: this.profileData.yearsOfExperience,
        skills: this.profileData.skills,
        languages: this.profileData.languages || '',
        linkedinUrl: this.profileData.linkedinUrl || ''
      };
      this.mode = 'create';
    }
  }

  onSubmit() {
    this.isSaving = true;
    const userId = Number(this.authService.getUserId());
    
    this.mentorService.createProfile(userId, this.formData).subscribe({
      next: (res) => {
        this.profileData = res;
        this.mode = 'view';
        this.isSaving = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error(err);
        this.isSaving = false;
        alert('Error saving profile');
        this.cdr.detectChanges();
      }
    });
  }
  
  goBack() {
    this.router.navigate(['/mentor-dashboard']);
  }
}