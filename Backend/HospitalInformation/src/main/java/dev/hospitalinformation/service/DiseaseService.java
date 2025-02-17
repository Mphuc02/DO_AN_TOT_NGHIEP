package dev.hospitalinformation.service;

import dev.common.constant.ExceptionConstant.*;
import dev.common.dto.response.insformation.DiseaseResponse;
import dev.common.exception.NotFoundException;
import dev.hospitalinformation.dto.request.CreateDiseaseRequest;
import dev.hospitalinformation.dto.request.UpdateDiseaseRequest;
import dev.hospitalinformation.entity.Disease;
import dev.hospitalinformation.repository.DiseaseRepository;
import dev.hospitalinformation.util.DiseaseUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DiseaseService {
    private final DiseaseRepository diseaseRepository;
    private final DiseaseUtil diseaseUtil;

    public List<DiseaseResponse> getAll(){
        return diseaseUtil.mapEntitiesToResponses(diseaseRepository.findAll());
    }

    public List<UUID> checkDiseasesExist(List<UUID> diseaseIds){
        return diseaseRepository.findAllById(diseaseIds).stream().map(Disease::getId).toList();
    }

    @Transactional
    public DiseaseResponse save(CreateDiseaseRequest request){
        Disease disease = diseaseUtil.mapCreateRequestToEntity(request);
        disease = diseaseRepository.save(disease);
        return diseaseUtil.mapEntityToResponse(disease);
    }

    @Transactional
    public DiseaseResponse update(UpdateDiseaseRequest request, UUID id){
        Disease findToUpdate = diseaseRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException(HOSPITAL_INFORMATION_EXCEPTION.DISEASE_NOT_FOUND));
        diseaseUtil.mapUpdateRequestToEntity(request, findToUpdate);
        findToUpdate = diseaseRepository.save(findToUpdate);
        return diseaseUtil.mapEntityToResponse(findToUpdate);
    }
}