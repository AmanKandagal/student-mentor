import { Injectable, signal, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

export interface ChatMessage {
  id?: number;
  text: string;
  senderId: string;
  targetId: string;
  timestamp: string | number;
}

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private http = inject(HttpClient);
  private socket: WebSocket | null = null;
  private readonly WS_URL = 'ws://localhost:8080/chat';
  private readonly API_URL = 'http://localhost:8080/api/chat';

  messages = signal<ChatMessage[]>([]);
  connectionStatus = signal<'CONNECTED' | 'DISCONNECTED' | 'CONNECTING'>('DISCONNECTED');
  activeChatUserId = signal<string | null>(null);
  unreadCounts = signal<Record<string, number>>({});

  constructor() {}

  loadChatHistory(myId: string, otherId: string) {
    this.http.get<any[]>(`${this.API_URL}/history/${myId}/${otherId}`).subscribe({
      next: (history) => {
        const mappedHistory: ChatMessage[] = history.map(msg => ({
            id: msg.id,
            text: msg.content || msg.text || msg.message || '', 
            senderId: String(msg.senderId || msg.sender_id || ''), 
            targetId: String(msg.recipientId || msg.recipient_id || msg.targetId || msg.target_id || ''),
            timestamp: msg.timestamp || msg.created_at || Date.now()
        }));

        this.messages.update(currentMsgs => {
          const optimisticMessages = currentMsgs.filter(m => !m.id);
          
          const combined = [...mappedHistory, ...optimisticMessages];
          
          const unique = combined.filter((msg, index, self) => 
            index === self.findIndex((m) => {
              const sameId = m.id && msg.id && m.id === msg.id;
              const sameContent = m.text === msg.text && String(m.senderId) === String(msg.senderId);
              const timeDiff = Math.abs(new Date(m.timestamp).getTime() - new Date(msg.timestamp).getTime());
              const sameTime = timeDiff < 2000; 

              return sameId || (sameContent && sameTime);
            })
          );

          return unique.sort((a, b) => {
             return new Date(a.timestamp).getTime() - new Date(b.timestamp).getTime();
          });
        });
      },
      error: (err) => console.error('❌ Failed to load chat history:', err)
    });
  }

  connect(userId: string) {
    if (this.socket && (this.socket.readyState === WebSocket.OPEN || this.socket.readyState === WebSocket.CONNECTING)) {
      return;
    }

    this.connectionStatus.set('CONNECTING');
    this.socket = new WebSocket(`${this.WS_URL}?userId=${userId}`);

    this.socket.onopen = () => {
      console.log('✅ WebSocket Connected');
      this.connectionStatus.set('CONNECTED');
    };

    this.socket.onmessage = (event) => {
      try {
        const raw = JSON.parse(event.data);
        const message: ChatMessage = {
          id: raw.id, 
          text: raw.text,
          senderId: String(raw.senderId),
          targetId: String(raw.targetId),
          timestamp: raw.timestamp
        };

        
        const isDuplicate = this.messages().some(existing => 
            existing.text === message.text && 
            String(existing.senderId) === String(message.senderId) &&
            Math.abs(new Date(existing.timestamp).getTime() - new Date(message.timestamp).getTime()) < 2000
        );

        if (isDuplicate) {
            
            return;
        }

        this.messages.update(msgs => [...msgs, message]);
        
        if (String(message.targetId) === String(userId) && 
            String(message.senderId) !== String(this.activeChatUserId())) {
           this.incrementUnread(message.senderId);
        }
      } catch (e) { console.error('Error parsing WS message', e); }
    };
    
    this.socket.onclose = () => {
      this.connectionStatus.set('DISCONNECTED');
      this.socket = null;
    };
  }

  disconnect() {
    if (this.socket) {
      this.socket.close();
      this.socket = null;
    }
    this.connectionStatus.set('DISCONNECTED');
  }

  sendMessage(text: string, senderId: string, targetId: string) {
    if (this.socket && this.socket.readyState === WebSocket.OPEN) {
      const payload = { 
        text, 
        senderId: String(senderId), 
        targetId: String(targetId), 
        timestamp: new Date().toISOString() 
      };
      
      // 1. Send to Server
      this.socket.send(JSON.stringify(payload));
      
     
      this.messages.update(msgs => [...msgs, payload]);
      
    } else {
      console.warn('Cannot send message: WebSocket is not open.');
    }
  }

  // --- Helper Methods ---
  setActiveChat(senderId: string) {
    this.activeChatUserId.set(String(senderId));
    this.clearUnread(String(senderId));
  }
  
  clearActiveChat() { 
    this.activeChatUserId.set(null); 
  }

  getUnreadCount(senderId: string) { 
    return this.unreadCounts()[String(senderId)] || 0; 
  }

  private incrementUnread(id: string) { 
    this.unreadCounts.update(c => ({...c, [id]: (c[id]||0)+1})); 
  }

  private clearUnread(id: string) { 
    this.unreadCounts.update(c => { 
      const n={...c}; 
      delete n[id]; 
      return n; 
    }); 
  }
}