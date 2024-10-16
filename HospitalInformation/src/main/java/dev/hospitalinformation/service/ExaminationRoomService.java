package dev.hospitalinformation.service;

import dev.common.constant.ExceptionConstant.*;
import dev.common.dto.response.ExaminationRoomCommonResponse;
import dev.common.exception.NotFoundException;
import dev.hospitalinformation.dto.request.CreateExaminationRoomRequest;
import dev.hospitalinformation.dto.request.UpdateExaminationRoomRequest;
import dev.hospitalinformation.entity.ExaminationRoom;
import dev.hospitalinformation.repository.ExaminationRoomRepository;
import dev.hospitalinformation.util.ExaminationRoomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExaminationRoomService {
    private final ExaminationRoomRepository examinationRoomRepository;
    private final ExaminationRoomUtil examinationRoomUtil;

    public List<ExaminationRoomCommonResponse> getAll(){
        return examinationRoomUtil.mapEntitiesToResponses(examinationRoomRepository.findAll());
    }

    public boolean checkRoomExist(UUID id){
        return examinationRoomRepository.existsById(id);
    }

    public ExaminationRoomCommonResponse save(CreateExaminationRoomRequest request){
        ExaminationRoom entity = examinationRoomUtil.mapCreateRequestToEntity(request);
        entity = examinationRoomRepository.save(entity);
        return examinationRoomUtil.mapEntityToResponse(entity);
    }

    public List<ExaminationRoomCommonResponse> findByIds(List<UUID> ids){
        return examinationRoomUtil.mapEntitiesToResponses(examinationRoomRepository.findAllById(ids));
    }

    public ExaminationRoomCommonResponse update(UpdateExaminationRoomRequest request, UUID id){
        ExaminationRoom findToUpdate = examinationRoomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(HOSPITAL_INFORMATION_EXCEPTION.EXAMINATION_ROOM_NOT_FOUND));
        examinationRoomUtil.mapUpdateRequestToEntity(request, findToUpdate);
        findToUpdate = examinationRoomRepository.save(findToUpdate);
        return examinationRoomUtil.mapEntityToResponse(findToUpdate);
    }
}