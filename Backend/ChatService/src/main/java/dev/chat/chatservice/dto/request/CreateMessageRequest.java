package dev.chat.chatservice.dto.request;

import dev.common.model.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldNameConstants
public class CreateMessageRequest {
    @NotNull(message = "Người nhận không được bỏ trống")
    private UUID receiverId;

    @NotNull(message = "Nội dung không được bỏ trống")
    private String content;
}