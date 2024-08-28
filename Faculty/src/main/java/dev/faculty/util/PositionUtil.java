package dev.faculty.util;

import dev.common.dto.response.PositionResponse;
import dev.faculty.dto.request.CreatePositionRequest;
import dev.faculty.dto.request.UpdatePositionRequest;
import dev.faculty.entity.Faculty;
import dev.faculty.entity.Position;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PositionUtil {
    public Position createRequestToEntity(CreatePositionRequest request){
        Faculty faculty = Faculty.builder().id(request.getFacultyId()).build();
        return Position.builder()
                .quantity(request.getQuantity())
                .permission(request.getPermission())
                .faculty(faculty)
                .build();
    }

    public PositionResponse entityToResponse(Position entity){
        return PositionResponse.builder()
                .id(entity.getId())
                .quantity(entity.getQuantity())
                .permission(entity.getPermission())
                .build();
    }

    public List<PositionResponse> listEntitiesToResponses(List<Position> entities){
        return entities.stream().map(this::entityToResponse).collect(Collectors.toList());
    }

    public void updateRequestToEntity(UpdatePositionRequest request, Position entity){
        if(!ObjectUtils.isEmpty(request.getPermission()))
            entity.setPermission(request.getPermission());

        if(!ObjectUtils.isEmpty(request.getQuantity()))
            entity.setQuantity(request.getQuantity());

        if(!ObjectUtils.isEmpty(request.getFacultyId())){
            Faculty faculty = Faculty.builder().id(request.getFacultyId()).build();
            entity.setFaculty(faculty);
        }
    }
}