package dev.chat.chatservice.mapper;

import dev.chat.chatservice.entity.RelationShip;
import dev.common.dto.response.chat.RelationShipResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RelationShipMapper {
    RelationShipResponse mapEntityToResponse(RelationShip relationShip);
}