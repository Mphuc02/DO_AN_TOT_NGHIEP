package dev.chat.chatservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_message")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID senderId;
    private UUID receiverId;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "mediumtext")
    private String processedImage;

    @Column(columnDefinition = "mediumtext")
    private String image;

    private LocalDateTime createdAt;
}