package dev.chat.chatservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DetectImageRequest {
    private UUID id;
    private String image;
    private UUID senderId;
    private UUID receiverId;
}