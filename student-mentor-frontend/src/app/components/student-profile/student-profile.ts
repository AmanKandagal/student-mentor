import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { StudentService } from '../../services/student.service';
import { StudentProfileRequest, StudentProfileResponse } from '../../models/student.model';

@Component({
  selector: 'app-student-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './student-profile.html',
  styleUrls: ['./student-profile.scss']
})
export class StudentProfileComponent implements OnInit {
  mode: 'loading' | 'create' | 'view' = 'loading';
  profileData: StudentProfileResponse | null = null;
  isSaving = false;
  
  formData: StudentProfileRequest = {
    university: '',
    branch: '',
    graduationYear: 2025,
    cgpa: 0.0,
    skills: '',
    about: '',
    linkedinUrl: '',
    resumeUrl: ''
  };

  constructor(
    private authService: AuthService, 
    private studentService: StudentService,
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

    this.studentService.getProfile(userId).subscribe({
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
        university: this.profileData.university,
        branch: this.profileData.branch,
        graduationYear: this.profileData.graduationYear,
        cgpa: this.profileData.cgpa,
        skills: this.profileData.skills,
        about: this.profileData.about,
        linkedinUrl: this.profileData.linkedinUrl || '',
        resumeUrl: this.profileData.resumeUrl || ''
      };
      this.mode = 'create';
    }
  }

  onSubmit() {
    this.isSaving = true;
    const userId = Number(this.authService.getUserId());
    
    this.studentService.createProfile(userId, this.formData).subscribe({
      next: (res) => {
        this.profileData = res;
        this.mode = 'view';
        this.isSaving = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error(err);
        this.isSaving = false;
        alert('Error saving profile. Please try again.');
      }
    });
  }
  
  goBack() {
    this.router.navigate(['/student-dashboard']);
  }
}