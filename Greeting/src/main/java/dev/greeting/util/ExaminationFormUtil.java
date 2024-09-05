package dev.greeting.util;

import dev.greeting.dto.request.CreateExaminationFormRequest;
import dev.greeting.entity.ExaminationForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExaminationFormUtil {
    public ExaminationForm createRequestToEntity(CreateExaminationFormRequest request){
        return ExaminationForm.builder()
                .patientId(request.getPatientId())
                .symptom(request.getSymptom())
                .build();
    }
}