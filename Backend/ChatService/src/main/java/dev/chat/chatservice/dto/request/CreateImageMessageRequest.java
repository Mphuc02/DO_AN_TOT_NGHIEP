package dev.chat.chatservice.dto.request;

import dev.common.model.Role;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateImageMessageRequest {
    private String image;
    private UUID receiverId;
    private UUID senderId;
    private Role role;
}