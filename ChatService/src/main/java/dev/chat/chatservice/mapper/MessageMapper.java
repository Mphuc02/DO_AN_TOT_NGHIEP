package dev.chat.chatservice.mapper;

import dev.chat.chatservice.dto.request.CreateMessageRequest;
import dev.chat.chatservice.dto.request.DetectImageRequest;
import dev.chat.chatservice.entity.Message;
import dev.common.dto.response.chat.MessageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MessageMapper {
    Message mapCreateRequestToEntity(CreateMessageRequest request);
    MessageResponse mapEntityToResponse(Message message);
    DetectImageRequest mapEntityToImageRequest(Message message);
}