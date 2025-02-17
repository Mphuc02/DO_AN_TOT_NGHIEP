package dev.common.dto.response.chat;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MessageResponse {
    private UUID id;
    private UUID senderId;
    private UUID receiverId;
    private String content;
    private String imageUrl;
    private String detectedImageUrl;
    private UUID relationShipId;
    private LocalDateTime createdAt;
}