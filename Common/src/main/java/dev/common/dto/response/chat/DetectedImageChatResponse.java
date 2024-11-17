package dev.common.dto.response.chat;

import lombok.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DetectedImageChatResponse {
    private UUID id;
    private String image;
    private String processedImage;
    private UUID senderId;
    private UUID receiverId;
}