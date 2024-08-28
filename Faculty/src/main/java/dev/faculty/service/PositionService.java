package dev.faculty.service;

import dev.common.constant.ExceptionConstant.*;
import dev.common.dto.response.PositionResponse;
import dev.common.exception.NotFoundException;
import dev.faculty.dto.request.CreatePositionRequest;
import dev.faculty.dto.request.UpdatePositionRequest;
import dev.faculty.entity.Position;
import dev.faculty.repository.PositionRepository;
import dev.faculty.util.PositionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PositionService {
    private final PositionRepository positionRepository;
    private final PositionUtil positionUtil;

    public List<PositionResponse> getByFaculty(UUID id){
        return positionUtil.listEntitiesToResponses(positionRepository.findAllByFacultyId(id));
    }

    public PositionResponse getById(UUID id){
        Position entity = positionRepository.findById(id).orElseThrow(() -> new NotFoundException(FACULTY_EXCEPTION.POSITION_NOT_FOUND));
        return positionUtil.entityToResponse(entity);
    }

    public PositionResponse save(CreatePositionRequest request){
        Position entity = positionUtil.createRequestToEntity(request);
        entity = positionRepository.save(entity);
        return positionUtil.entityToResponse(entity);
    }

    public PositionResponse update(UpdatePositionRequest request, UUID id){
        Position findToUpdate = positionRepository.findById(id).orElseThrow(() -> new NotFoundException(FACULTY_EXCEPTION.POSITION_NOT_FOUND));
        positionUtil.updateRequestToEntity(request, findToUpdate);
        findToUpdate = positionRepository.save(findToUpdate);
        return positionUtil.entityToResponse(findToUpdate);
    }

    public void delete(UUID id){
        if(!positionRepository.existsById(id))
            throw new NotFoundException(FACULTY_EXCEPTION.POSITION_NOT_FOUND);
        positionRepository.deleteById(id);
    }
}