package dev.chat.chatservice.repository;

import dev.chat.chatservice.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    @Query(value = """
        Select m from Message m
        where m.receiverId = :receiverId
        and m.senderId = :senderId
        order by m.createdAt desc
    """)
    Page<Message> findByReceiverId(UUID receiverId, UUID senderId, Pageable pageable);
}