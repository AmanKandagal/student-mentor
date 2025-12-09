package com.studnetmentor.student_mentor_backend.repositories;

import com.studnetmentor.student_mentor_backend.models.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderIdAndRecipientIdOrSenderIdAndRecipientIdOrderByTimestampAsc(
        String senderId, String recipientId, String senderId2, String recipientId2
    );
}