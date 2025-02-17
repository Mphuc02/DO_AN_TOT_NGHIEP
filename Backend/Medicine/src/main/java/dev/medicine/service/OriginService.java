package dev.medicine.service;

import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.NotFoundException;
import dev.medicine.dto.request.create.CreateOriginRequest;
import dev.medicine.dto.request.update.UpdateOriginRequest;
import dev.common.dto.response.medicine.OriginResponse;
import dev.medicine.entity.Origin;
import dev.medicine.repository.OriginRepository;
import dev.medicine.util.OriginMapperUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OriginService {
    private final OriginRepository originRepository;
    private final OriginMapperUtil originMapperUtil;

    public List<OriginResponse> getAll(){
        return originMapperUtil.mapEntitiesToResponses(originRepository.findAll());
    }

    @Transactional
    public OriginResponse save(CreateOriginRequest request){
        Origin origin = originMapperUtil.mapCreateRequestToEntity(request);
        origin = originRepository.save(origin);
        return originMapperUtil.mapEntityToResponse(origin);
    }

    @Transactional
    public OriginResponse update(UpdateOriginRequest request, UUID id){
        Origin origin = originRepository.findById(id).orElseThrow(() -> new NotFoundException(MEDICINE_EXCEPTION.ORIGIN_NOT_FOUND));
        originMapperUtil.mapUpdateRequestToEntity(request, origin);
        origin = originRepository.save(origin);
        return originMapperUtil.mapEntityToResponse(origin);
    }
}