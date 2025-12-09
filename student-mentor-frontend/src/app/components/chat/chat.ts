import { Component, OnInit, Input, ElementRef, ViewChild, inject, computed, effect, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChatService } from '../../services/chat.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chat.html',
  styleUrls: ['./chat.scss']
})
export class ChatComponent implements OnInit {
  
  // 1. Reactive Signal for Target ID
  private _targetId = signal<string>('');
  @Input() set targetId(val: string) {
    this._targetId.set(String(val));
  }
  get targetId() { return this._targetId(); }

  @Input() targetName: string = 'Chat'; 

  chatService = inject(ChatService);
  authService = inject(AuthService);
  
  // 2. Reactive Signal for My ID
  myUserId = signal<string>('');
  
  newMessage = '';
  @ViewChild('scrollContainer') private scrollContainer!: ElementRef;

  // 3. Filter Logic
  filteredMessages = computed(() => {
    const allMsgs = this.chatService.messages();
    const me = this.myUserId();
    const them = this._targetId();

    return allMsgs.filter(msg => {
      const msgSender = String(msg.senderId);
      const msgTarget = String(msg.targetId);
      
      return (msgSender === me && msgTarget === them) || 
             (msgSender === them && msgTarget === me);
    });
  });

  constructor() {
    // 4. AUTO-LOAD HISTORY EFFECT
    effect(() => {
      const me = this.myUserId();
      const them = this._targetId();

      if (me && them) {
        this.chatService.loadChatHistory(me, them);
      }
    }, { allowSignalWrites: true });

    // Auto-scroll effect
    effect(() => {
      const count = this.filteredMessages().length;
      setTimeout(() => this.scrollToBottom(), 100);
    });
  }

  ngOnInit() {
    const id = this.authService.getUserId();
    if (id) {
      this.myUserId.set(String(id));
    }
  }

  sendMessage() {
    if (!this.newMessage.trim() || !this.targetId) return;
    this.chatService.sendMessage(this.newMessage, this.myUserId(), this.targetId);
    this.newMessage = '';
  }

  scrollToBottom(): void {
    try {
      this.scrollContainer.nativeElement.scrollTop = this.scrollContainer.nativeElement.scrollHeight;
    } catch(err) { }
  }
}