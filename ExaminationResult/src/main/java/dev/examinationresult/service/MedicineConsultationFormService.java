package dev.examinationresult.service;

import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.BaseException;
import dev.common.exception.NotFoundException;
import dev.common.exception.NotPermissionException;
import dev.common.util.AuditingUtil;
import dev.examinationresult.dto.request.SaveMedicineConsultationFormRequest;
import dev.examinationresult.dto.request.SaveMedicineConsultationFormDetailRequest;
import dev.common.dto.response.examination_result.MedicineConsultationFormResponse;
import dev.examinationresult.entity.MedicineConsultationForm;
import dev.examinationresult.entity.MedicineConsultationFormDetail;
import dev.examinationresult.repository.MedicineConsultationFormDetailRepository;
import dev.examinationresult.repository.MedicineConsultationFormRepository;
import dev.examinationresult.util.MedicineConsultationFormDetailMapperUtil;
import dev.examinationresult.util.MedicineConsultationFormMapperUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicineConsultationFormService {
    private final MedicineConsultationFormRepository formRepository;
    private final MedicineConsultationFormDetailRepository formDetailRepository;
    private final MedicineConsultationFormMapperUtil formMapperUtil;
    private final MedicineConsultationFormDetailMapperUtil formDetailMapperUtil;
    private final AuditingUtil auditingUtil;

    public List<MedicineConsultationFormResponse> getAll(){
        return formMapperUtil.mapEntitiesToResponses(formRepository.findAll());
    }

    @Transactional
    public MedicineConsultationFormResponse create(SaveMedicineConsultationFormRequest request){
        if(formRepository.existsById(request.getId())){
            throw BaseException.buildBadRequest().message(EXAMINATION_RESULT_EXCEPTION.MEDICINE_CONSULTATION_EXISTED).build();
        }

        UUID employeeId = auditingUtil.getUserLogged().getId();
        MedicineConsultationForm form = formMapperUtil.mapCreateRequestToEntity(request);
        form.setEmployeeId(employeeId);
        form.setCreatedAt(LocalDateTime.now());
        form = formRepository.save(form);
        handleSaveFormDetail(request.getDetails(), form);
        return formMapperUtil.mapEntityToResponse(form);
    }

    public MedicineConsultationFormResponse findById(UUID id){
        MedicineConsultationForm form = formRepository.findById(id).orElseThrow(() -> BaseException.buildNotFound().message(EXAMINATION_RESULT_EXCEPTION.CONSULTATION_FORM_NOT_FOUND).build());
        return formMapperUtil.mapEntityToResponse(form);
    }

    @Transactional
    public MedicineConsultationFormResponse update(SaveMedicineConsultationFormRequest request,
                                                   UUID id){
        MedicineConsultationForm findToUpdate = formRepository.findById(id)
                                                    .orElseThrow(() -> new NotFoundException(EXAMINATION_RESULT_EXCEPTION.CONSULTATION_FORM_NOT_FOUND));

        if(!Objects.equals(findToUpdate.getEmployeeId(), auditingUtil.getUserLogged().getId()))
            throw new NotPermissionException(EXAMINATION_RESULT_EXCEPTION.NOT_CONSULTATION_FORM_OWNER);

        List<MedicineConsultationFormDetail> oldDetails = findToUpdate.getDetails();
        formDetailRepository.deleteAll(oldDetails);

        formMapperUtil.mapUpdateRequestToEntity(request, findToUpdate);
        findToUpdate = formRepository.save(findToUpdate);
        handleSaveFormDetail(request.getDetails(), findToUpdate);
        return formMapperUtil.mapEntityToResponse(findToUpdate);
    }

    @Transactional
    public void handleSaveFormDetail(List<SaveMedicineConsultationFormDetailRequest> detailRequests, MedicineConsultationForm form){
        Map<UUID, MedicineConsultationFormDetail> details = detailRequests.stream().map(request -> {
            MedicineConsultationFormDetail detail = formDetailMapperUtil.mapSaveRequestToEntity(request);
            detail.setForm(form);
            return detail;
        }).collect(Collectors.toMap(MedicineConsultationFormDetail::getMedicineId,
                                        detail -> detail,
                                        (detail1, detail2) -> {
                                            detail2.setQuantity(detail1.getQuantity() + detail2.getQuantity());
                                            return detail2;
                                        }));
        formDetailRepository.saveAll(details.values());
    }
}