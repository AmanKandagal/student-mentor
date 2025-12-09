import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { RegisterComponent } from './components/register/register';
import { StudentDashboardComponent } from './components/student-dashboard/student-dashboard';
import { MentorDashboardComponent } from './components/mentor-dashboard/mentor-dashboard';
import { StudentProfileComponent } from './components/student-profile/student-profile'; 
import { MentorProfileComponent } from './components/mentor-profile/mentor-profile'; 
import { FindMentorComponent } from './components/find-mentor/find-mentor';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'student-dashboard', component: StudentDashboardComponent },
  { path: 'mentor-dashboard', component: MentorDashboardComponent },
  { path: 'student-profile', component: StudentProfileComponent },
  { path: 'mentor-profile', component: MentorProfileComponent },
  { path: 'find-mentor', component: FindMentorComponent }, // <--- Add this line
];