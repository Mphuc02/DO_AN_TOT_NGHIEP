package dev.chat.chatservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateMessageFirstTimeRequest{
    @NotNull(message = "Họ tên người thứ nhất không được bỏ trống")
    private String firstUserFullName;

    @NotNull(message = "Người thứ 2 không được bỏ trống")
    private UUID secondUserId;

    @NotNull(message = "Họ tên người thứ 2 không được bỏ trống")
    private String secondUserFullName;
    
    private String content;
    private String image;
}