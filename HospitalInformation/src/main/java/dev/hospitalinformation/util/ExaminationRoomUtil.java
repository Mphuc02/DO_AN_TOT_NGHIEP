package dev.hospitalinformation.util;

import dev.common.dto.response.ExaminationRoomCommonResponse;
import dev.hospitalinformation.dto.request.CreateExaminationRoomRequest;
import dev.hospitalinformation.dto.request.UpdateExaminationRoomRequest;
import dev.hospitalinformation.entity.ExaminationRoom;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ExaminationRoomUtil {
    public ExaminationRoom mapCreateRequestToEntity(CreateExaminationRoomRequest request){
        return ExaminationRoom.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .build();
    }

    public void mapUpdateRequestToEntity(UpdateExaminationRoomRequest request, ExaminationRoom room){
        if(!ObjectUtils.isEmpty(request.getName()))
            room.setName(request.getName());
    }

    public ExaminationRoomCommonResponse mapEntityToResponse(ExaminationRoom room){
        return ExaminationRoomCommonResponse.builder()
                .id(room.getId())
                .name(room.getName())
                .build();
    }

    public List<ExaminationRoomCommonResponse> mapEntitiesToResponses(List<ExaminationRoom> rooms){
        return rooms.stream().map(this::mapEntityToResponse).collect(Collectors.toList());
    }
}