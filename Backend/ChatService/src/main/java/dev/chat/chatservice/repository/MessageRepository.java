package dev.chat.chatservice.repository;

import dev.chat.chatservice.entity.Message;
import dev.chat.chatservice.entity.RelationShip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    Page<Message> findByRelationShipOrderByCreatedAtDesc(RelationShip relationShip, Pageable pageable);
}