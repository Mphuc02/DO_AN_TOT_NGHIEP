package dev.greeting.util;

import dev.common.dto.request.CreateExaminationResultCommonRequest;
import dev.common.dto.response.ExaminationFormResponse;
import dev.greeting.dto.request.CreateFormWithoutAppointmentRequest;
import dev.greeting.entity.ExaminationForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.sql.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExaminationFormUtil {
    public ExaminationForm createRequestToEntity(CreateFormWithoutAppointmentRequest request){
        return ExaminationForm.builder()
                .id(UUID.randomUUID())
                .patientId(request.getPatient().getId())
                .symptom(request.getSymptom())
                .ticketIndex(request.getNumberCall())
                .workingScheduleId(request.getWorkingSchedule())
                .createdAt(new Date(new java.util.Date().getTime()))
                .build();
    }

    public ExaminationFormResponse entityToResponse(ExaminationForm entity){
        return ExaminationFormResponse.builder()
                .id(entity.getId())
                .ticketIndex(entity.getTicketIndex())
                .employeeId(entity.getEmployeeId())
                .patientId(entity.getPatientId())
                .createdAt(entity.getCreatedAt())
                .symptom(entity.getSymptom())
                .build();
    }

    public CreateExaminationResultCommonRequest buildCreateExaminationResultRequest(ExaminationForm form){
        return CreateExaminationResultCommonRequest.builder()
                .id(form.getId())
                .workingScheduleId(form.getWorkingScheduleId())
                .patientId(form.getPatientId())
                .build();
    }
}