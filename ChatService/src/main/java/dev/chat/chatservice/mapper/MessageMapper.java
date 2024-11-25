package dev.chat.chatservice.mapper;

import dev.chat.chatservice.dto.request.CreateMessageRequest;
import dev.chat.chatservice.dto.request.DetectImageRequest;
import dev.chat.chatservice.entity.Message;
import dev.common.dto.response.chat.MessageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MessageMapper {
    Message mapCreateRequestToEntity(CreateMessageRequest request);
    MessageResponse mapEntityToResponse(Message message);

    @Mapping(source = "relationShipId", target = "relationShipId", qualifiedByName = "mapRelationShipId")
    MessageResponse mapEntityToResponse(Message message, UUID relationShipId);
    DetectImageRequest mapEntityToImageRequest(Message message);

    @Named("mapRelationShipId")
    default UUID mapRelationShipId(UUID relationShipId){
        return relationShipId;
    }
}